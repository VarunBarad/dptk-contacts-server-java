package com.dptradeking.all;

import com.dptradeking.model.Branch;
import com.dptradeking.model.Department;
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
 * Creator: vbarad
 * Date: 2016-08-05
 * Project: DP-TradeKING
 */
@WebServlet(name = "CombinedServlet")
public class CombinedServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    MongoClient mongoClient = new MongoClient(this.getServletContext().getInitParameter("database-host"));
    MongoDatabase mongoDatabase = mongoClient.getDatabase(this.getServletContext().getInitParameter("database-name"));

    FindIterable<Document> subBrokerIterable = mongoDatabase.getCollection("subBrokers").find();

    final ArrayList<SubBroker> subBrokers = new ArrayList<>();

    subBrokerIterable.forEach(new Block<Document>() {

      @Override
      public void apply(Document document) {
        SubBroker subBroker = SubBroker.SubBrokerFactory(document.toJson());
        subBroker.populateId();
        subBrokers.add(subBroker);
      }
    });

    FindIterable<Document> branchIterable = mongoDatabase.getCollection("branches").find();

    final ArrayList<Branch> branches = new ArrayList<>();

    branchIterable.forEach(new Block<Document>() {

      @Override
      public void apply(Document document) {
        Branch branch = Branch.BranchFactory(document.toJson());
        branch.populateId();
        branches.add(branch);
      }
    });

    FindIterable<Document> departmentIterable = mongoDatabase.getCollection("headOffice").find();

    final ArrayList<Department> departments = new ArrayList<>();

    departmentIterable.forEach(new Block<Document>() {

      @Override
      public void apply(Document document) {
        Department department = Department.DepartmentFactory(document.toJson());
        department.populateId();
        departments.add(department);
      }
    });

    JSONObject message = new JSONObject();
    message.put("subBrokers", new JSONArray((new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()).toJson(subBrokers)));
    message.put("branches", new JSONArray((new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()).toJson(branches)));
    message.put("headOffice", new JSONArray((new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()).toJson(departments)));

    JSONObject responseJson = new JSONObject();
    responseJson.put("status", 200);
    responseJson.put("message", message);

    PrintWriter responseWriter = response.getWriter();

    responseWriter.print(responseJson.toString());
    responseWriter.close();
  }
}
