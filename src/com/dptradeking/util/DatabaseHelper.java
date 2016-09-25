package com.dptradeking.util;

import com.dptradeking.model.Branch;
import com.dptradeking.model.Department;
import com.dptradeking.model.SubBroker;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

/**
 * Creator: vbarad
 * Date: 2016-08-29
 * Project: DP-TradeKING
 */
public class DatabaseHelper {
  private MongoClient mongoClient;
  private MongoDatabase mongoDatabase;
  
  public DatabaseHelper(String hostName, String databaseName) {
    this.mongoClient = new MongoClient(hostName);
    this.mongoDatabase = mongoClient.getDatabase(databaseName);
  }
  
  public ArrayList<SubBroker> getSubBrokers() {
    FindIterable<Document> iterable = this.mongoDatabase.getCollection("subBrokers").find();
    final ArrayList<SubBroker> subBrokers = new ArrayList<>();
    
    iterable
        .map(SubBroker::getInstance)
        .forEach((Block<? super SubBroker>) subBrokers::add);
    
    subBrokers
        .stream()
        .sorted((s1, s2) -> s1.getName().compareTo(s2.getName()));
    
    return subBrokers;
  }
  
  public String insertSubBroker(SubBroker subBroker) {
    Document subBrokerDocument = subBroker.toDocument();
    this.mongoDatabase.getCollection("subBrokers")
        .insertOne(subBrokerDocument);
    
    String insertId = subBrokerDocument.getObjectId("_id").toHexString();
    return insertId;
  }
  
  public void insertMultipleSubBrokers(ArrayList<SubBroker> subBrokers) {
    MongoCollection collection = this.mongoDatabase.getCollection("subBrokers");
    
    subBrokers
        .stream()
        .map(SubBroker::toDocument)
        .forEach(collection::insertOne);
  }
  
  public ArrayList<Branch> getBranches() {
    FindIterable<Document> iterable = this.mongoDatabase.getCollection("branches").find();
    final ArrayList<Branch> branches = new ArrayList<>();
    
    iterable
        .map(Branch::getInstance)
        .forEach((Block<? super Branch>) branches::add);
    
    branches
        .stream()
        .sorted((b1, b2) -> b1.getName().compareTo(b2.getName()));
    
    return branches;
  }
  
  public String insertBranch(Branch branch) {
    Document branchDocument = branch.toDocument();
    this.mongoDatabase.getCollection("branches")
        .insertOne(branchDocument);
    
    String insertId = branchDocument.getObjectId("_id").toHexString();
    return insertId;
  }
  
  public void insertMultipleBranches(ArrayList<Branch> branches) {
    MongoCollection collection = this.mongoDatabase.getCollection("branches");
    
    branches
        .stream()
        .map(Branch::toDocument)
        .forEach(collection::insertOne);
  }
  
  public ArrayList<Department> getDepartments() {
    FindIterable<Document> iterable = this.mongoDatabase.getCollection("headOffice").find();
    final ArrayList<Department> departments = new ArrayList<>();
    
    iterable
        .map(Department::getInstance)
        .forEach((Block<? super Department>) departments::add);
    
    departments
        .stream()
        .sorted((d1, d2) -> d1.getName().compareTo(d2.getName()));
    
    return departments;
  }
  
  public String insertDepartment(Department department) {
    Document departmentDocument = department.toDocument();
    this.mongoDatabase.getCollection("headOffice")
        .insertOne(departmentDocument);
    
    String insertId = departmentDocument.getObjectId("_id").toHexString();
    return insertId;
  }
  
  public void insertMultipleDepartments(ArrayList<Department> departments) {
    MongoCollection collection = this.mongoDatabase.getCollection("headOffice");
    
    departments
        .stream()
        .map(Department::toDocument)
        .forEach(collection::insertOne);
  }
  
  public void clearDatabase() {
    Document conditionDocument = new Document();
    
    this.mongoDatabase
        .getCollection("subBrokers")
        .deleteMany(conditionDocument);
    
    this.mongoDatabase
        .getCollection("branches")
        .deleteMany(conditionDocument);
    
    this.mongoDatabase
        .getCollection("headOffice")
        .deleteMany(conditionDocument);
  }
}
