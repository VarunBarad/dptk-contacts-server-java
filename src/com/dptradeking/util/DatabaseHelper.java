package com.dptradeking.util;

import com.dptradeking.model.Branch;
import com.dptradeking.model.Department;
import com.dptradeking.model.SubBroker;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator: vbarad
 * Date: 2016-08-29
 * Project: DP-TradeKING
 */
public class DatabaseHelper {
  private MongoClient mongoClient;
  private MongoDatabase mongoDatabase;
  
  public DatabaseHelper(String hostName, String databaseName, String username, String password) {
    List<MongoCredential> credentials = new ArrayList<>();
    credentials.add(MongoCredential.createCredential(username, databaseName, password.toCharArray()));
    
    ServerAddress hostAddress = new ServerAddress(hostName);
    
    this.mongoClient = new MongoClient(hostAddress, credentials);
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
  
  public DatabaseHelper insertMultipleSubBrokers(ArrayList<SubBroker> subBrokers) {
    MongoCollection collection = this.mongoDatabase.getCollection("subBrokers");
    
    subBrokers
        .stream()
        .map(SubBroker::toDocument)
        .forEach(collection::insertOne);
    
    return this;
  }
  
  public DatabaseHelper insertMultipleBranches(ArrayList<Branch> branches) {
    MongoCollection collection = this.mongoDatabase.getCollection("branches");
    
    branches
        .stream()
        .map(Branch::toDocument)
        .forEach(collection::insertOne);
    
    return this;
  }
  
  public DatabaseHelper insertMultipleDepartments(ArrayList<Department> departments) {
    MongoCollection collection = this.mongoDatabase.getCollection("headOffice");
    
    departments
        .stream()
        .map(Department::toDocument)
        .forEach(collection::insertOne);
    
    return this;
  }
  
  public DatabaseHelper clearDatabase() {
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
    
    return this;
  }
  
  public void close() {
    this.mongoClient.close();
  }
}
