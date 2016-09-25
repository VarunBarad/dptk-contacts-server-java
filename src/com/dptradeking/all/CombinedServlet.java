package com.dptradeking.all;

import com.dptradeking.config.Config;
import com.dptradeking.model.Branch;
import com.dptradeking.model.Department;
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

/**
 * Creator: vbarad
 * Date: 2016-08-05
 * Project: DP-TradeKING
 */
@WebServlet(name = "CombinedServlet")
public class CombinedServlet extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    DatabaseHelper databaseHelper = new DatabaseHelper(
        Config.Database.DB_HOST,
        Config.Database.DB_NAME
    );
  
    final ArrayList<Department> headOffice = databaseHelper.getDepartments();
    final ArrayList<Branch> branches = databaseHelper.getBranches();
    final ArrayList<SubBroker> subBrokers = databaseHelper.getSubBrokers();
  
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .create();
  
    JSONObject message = new JSONObject();
    message.put("subBrokers", new JSONArray(gson.toJson(subBrokers)));
    message.put("branches", new JSONArray(gson.toJson(branches)));
    message.put("headOffice", new JSONArray(gson.toJson(headOffice)));

    JSONObject responseJson = new JSONObject();
    responseJson.put("status", 200);
    responseJson.put("message", message);

    PrintWriter responseWriter = response.getWriter();

    responseWriter.print(responseJson.toString());
    responseWriter.close();
  }
}
