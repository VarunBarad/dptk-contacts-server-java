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

/**
 * Created by vbarad on 29/7/16.
 */
@WebServlet(name = "SubBrokersServlet")
public class SubBrokersServlet extends HttpServlet {
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

    JSONObject responseJson = new JSONObject();
    responseJson.put("status", 200);
    responseJson.put("message", new JSONArray((new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()).toJson(subBrokers)));

    PrintWriter responseWriter = response.getWriter();

    responseWriter.print(responseJson.toString());
    responseWriter.close();
  }
}
