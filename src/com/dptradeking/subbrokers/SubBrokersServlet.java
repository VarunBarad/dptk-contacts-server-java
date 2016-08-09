package com.dptradeking.subbrokers;

import com.dptradeking.model.SubBroker;
import com.google.gson.GsonBuilder;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Creator: vbarad
 * Date: 2016-08-05
 * Project: DP-TradeKING
 */
@WebServlet(name = "SubBrokersServlet")
public class SubBrokersServlet extends HttpServlet {
  private static final String PARAM_CATEGORIZE = "categorize";

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    MongoClient mongoClient = new MongoClient("localhost");
    MongoDatabase mongoDatabase = mongoClient.getDatabase("dptradeking");

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
