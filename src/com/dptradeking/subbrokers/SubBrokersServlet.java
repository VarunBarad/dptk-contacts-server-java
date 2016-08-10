package com.dptradeking.subbrokers;

import com.dptradeking.model.SubBroker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Creator: vbarad
 * Date: 2016-08-05
 * Project: DP-TradeKING
 */
@WebServlet(name = "SubBrokersServlet")
public class SubBrokersServlet extends HttpServlet {
  private static final String PARAM_CATEGORIZE = "categorize";

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Scanner bodyReader = new Scanner(request.getReader());
    bodyReader.useDelimiter("\\Z");
    String body = bodyReader.next();
    bodyReader.close();

    SubBroker subBroker = this.extractSubBroker(body);

    JSONObject responseJson = new JSONObject();
    if (subBroker != null) {
      String insertId = this.insertSubBroker(subBroker);
      if (insertId != null) {
        responseJson.put("status", 200);
        responseJson.put("message", insertId);
      } else {
        responseJson.put("status", 400);
        responseJson.put("message", "Incomplete/Illegal parameters");
      }
    } else {
      responseJson.put("status", 400);
      responseJson.put("message", "Incomplete/Illegal parameters");
    }

    response.getWriter().append(responseJson.toString()).close();
  }

  private SubBroker extractSubBroker(String body) {
    SubBroker subBroker = new SubBroker();

    try {
      JSONObject bodyJson = new JSONObject(body);

      String stringName = bodyJson.getString("name");
      if (SubBroker.validateName(stringName)) {
        subBroker.setName(stringName);
      } else {
        throw new IllegalArgumentException();
      }

      String stringAddress = bodyJson.getString("address");
      if (SubBroker.validateAddress(stringAddress)) {
        subBroker.setAddress(stringAddress);
      } else {
        throw new IllegalArgumentException();
      }

      String stringContactNumber = bodyJson.getString("contactNumber");
      if (SubBroker.validateContactNumber(stringContactNumber)) {
        subBroker.setContactNumber(stringContactNumber);
      } else {
        throw new IllegalArgumentException();
      }

      String stringEmail = bodyJson.getString("email");
      if (SubBroker.validateEmail(stringEmail)) {
        subBroker.setEmail(stringEmail);
      } else {
        throw new IllegalArgumentException();
      }

      String stringRegistrationNumber = bodyJson.getString("registrationNumber");
      if (SubBroker.validateRegistrationNumber(stringRegistrationNumber)) {
        subBroker.setRegistrationNumber(stringRegistrationNumber);
      } else {
        throw new IllegalArgumentException();
      }

      String stringIncorporationDate = bodyJson.getString("incorporationDate");
      if (SubBroker.validateIncorporationDate(stringIncorporationDate)) {
        subBroker.setIncorporationDate(stringIncorporationDate);
      } else {
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException | JSONException e) {
      e.printStackTrace();
      subBroker = null;
    }

    return subBroker;
  }

  private String insertSubBroker(SubBroker subBroker) {
    String insertId;

    try {
      Document subBrokerDocument = new Document();

      Map<String, Object> subBrokerMap = new Gson().fromJson(subBroker.toString(), new TypeToken<HashMap<String, Object>>() {
      }.getType());
      ObjectId subBrokerId = new ObjectId();
      subBrokerMap.remove("id");
      subBrokerDocument.putAll(subBrokerMap);
      subBrokerDocument.put("_id", subBrokerId);

      MongoClient mongoClient = new MongoClient(this.getServletContext().getInitParameter("database-host"));
      MongoDatabase mongoDatabase = mongoClient.getDatabase(this.getServletContext().getInitParameter("database-name"));

      mongoDatabase.getCollection("subBrokers").insertOne(subBrokerDocument);
      insertId = subBrokerId.toHexString();
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
      insertId = null;
    }

    return insertId;
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    MongoClient mongoClient = new MongoClient(this.getServletContext().getInitParameter("database-host"));
    MongoDatabase mongoDatabase = mongoClient.getDatabase(this.getServletContext().getInitParameter("database-name"));

    FindIterable<Document> iterable = mongoDatabase.getCollection("subBrokers").find();

    final ArrayList<SubBroker> subBrokers = new ArrayList<>();

    iterable.forEach(new Block<Document>() {

      @Override
      public void apply(Document document) {
        SubBroker subBroker = SubBroker.SubBrokerFactory(document.toJson());
        subBroker.populateId();
        subBrokers.add(subBroker);
      }
    });
    Collections.sort(subBrokers, (s1, s2) -> s1.getName().compareTo(s2.getName()));

    boolean categorize = false;
    if (Boolean.parseBoolean(request.getParameter(PARAM_CATEGORIZE))) {
      categorize = true;
    }

    JSONObject responseJson = new JSONObject();
    responseJson.put("status", 200);
    if (categorize) {
      JSONObject categorizedSubBrokers = this.categorizeSubBrokers(subBrokers);
      responseJson.put("message", categorizedSubBrokers);
    } else {
      responseJson.put("message", new JSONArray((new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()).toJson(subBrokers)));
    }

    PrintWriter responseWriter = response.getWriter();

    responseWriter.print(responseJson.toString());
    responseWriter.close();
  }

  private JSONObject categorizeSubBrokers(final ArrayList<SubBroker> subBrokers) {
    HashMap<Character, ArrayList<SubBroker>> mapSubBrokers = new HashMap<>();
    for (SubBroker s : subBrokers) {
      char key = Character.toUpperCase(s.getName().charAt(0));
      if (!mapSubBrokers.keySet().contains(key)) {
        mapSubBrokers.put(key, new ArrayList<>());
      }
      mapSubBrokers.get(key).add(s);
    }

    JSONObject categorizedSubBrokers = new JSONObject();
    for (char key : mapSubBrokers.keySet()) {
      categorizedSubBrokers.put(Character.toString(key), new JSONArray((new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()).toJson(mapSubBrokers.get(key))));
    }

    return categorizedSubBrokers;
  }
}
