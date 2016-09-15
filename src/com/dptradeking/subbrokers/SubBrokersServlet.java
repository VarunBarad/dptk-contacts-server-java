package com.dptradeking.subbrokers;

import com.dptradeking.model.SubBroker;
import com.dptradeking.util.DatabaseHelper;
import com.dptradeking.util.gsonadapter.ObjectIdAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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
      DatabaseHelper databaseHelper = new DatabaseHelper(
          this.getServletContext().getInitParameter("database-host"),
          this.getServletContext().getInitParameter("database-name")
      );
      String insertId = databaseHelper.insertSubBroker(subBroker);
      
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
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    DatabaseHelper databaseHelper = new DatabaseHelper(
        this.getServletContext().getInitParameter("database-host"),
        this.getServletContext().getInitParameter("database-name")
    );
    
    final ArrayList<SubBroker> subBrokers = databaseHelper.getSubBrokers();
    
    boolean categorize = Boolean.parseBoolean(request.getParameter(PARAM_CATEGORIZE));
    
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .create();
    JSONObject responseJson = new JSONObject();
    responseJson.put("status", 200);
    if (categorize) {
      JSONObject categorizedSubBrokers = this.categorizeSubBrokers(subBrokers);
      responseJson.put("message", categorizedSubBrokers);
    } else {
      responseJson.put("message", new JSONArray(gson.toJson(subBrokers)));
    }
    
    PrintWriter responseWriter = response.getWriter();
    
    responseWriter.print(responseJson.toString());
    responseWriter.close();
  }
  
  private JSONObject categorizeSubBrokers(final ArrayList<SubBroker> subBrokers) {
    HashMap<Character, ArrayList<SubBroker>> mapSubBrokers = new HashMap<>();
    
    Gson gson = (new GsonBuilder()).registerTypeAdapter(ObjectId.class, new ObjectIdAdapter()).excludeFieldsWithoutExposeAnnotation().create();
    
    subBrokers
        .parallelStream()
        .forEach(subBroker -> {
          char key = Character.toUpperCase(subBroker.getName().charAt(0));
          if (!mapSubBrokers.keySet().contains(key)) {
            mapSubBrokers.put(key, new ArrayList<>());
          }
          mapSubBrokers.get(key).add(subBroker);
        });
    
    JSONObject categorizedSubBrokers = new JSONObject();
    mapSubBrokers
        .keySet()
        .parallelStream()
        .forEach(key ->
            categorizedSubBrokers.put(Character.toString(key), new JSONArray(gson.toJson(mapSubBrokers.get(key))))
        );
    
    return categorizedSubBrokers;
  }
}
