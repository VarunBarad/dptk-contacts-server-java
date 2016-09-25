package com.dptradeking.subbrokers;

import com.dptradeking.model.SubBroker;
import com.dptradeking.util.DatabaseHelper;
import com.dptradeking.util.gsonadapter.ObjectIdAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.types.ObjectId;
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
import java.util.HashMap;

/**
 * Creator: vbarad
 * Date: 2016-08-05
 * Project: DP-TradeKING
 */
@WebServlet(name = "SubBrokersServlet")
public class SubBrokersServlet extends HttpServlet {
  private static final String PARAM_CATEGORIZE = "categorize";
  
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
