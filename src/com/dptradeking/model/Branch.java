package com.dptradeking.model;

import com.dptradeking.util.gsonadapter.ObjectIdAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Creator: vbarad
 * Date: 2016-08-05
 * Project: DP-TradeKING
 */
public class Branch {
  @Expose
  @SerializedName("_id")
  private ObjectId _id;
  @Expose
  @SerializedName("name")
  private String name;
  @Expose
  @SerializedName("alias")
  private String alias;
  @Expose
  @SerializedName("address")
  private String address;
  @Expose
  @SerializedName("contactNumber")
  private String contactNumber;
  @Expose
  @SerializedName("executives")
  private ArrayList<Executive> executives;
  
  public Branch() {
    this.executives = new ArrayList<>();
  }
  
  public Branch(String name, String alias, String address, String contactNumber) {
    this._id = new ObjectId();
    this.name = name;
    this.alias = alias;
    this.address = address;
    this.contactNumber = contactNumber;
    this.executives = new ArrayList<>();
  }
  
  public Branch(String id, String name, String alias, String address, String contactNumber) {
    this._id = new ObjectId(id);
    this.name = name;
    this.alias = alias;
    this.address = address;
    this.contactNumber = contactNumber;
    this.executives = new ArrayList<>();
  }
  
  public static Branch getInstance(String jsonBranch) {
    Gson gson = new Gson();
    Branch branch;
    try {
      branch = gson.fromJson(jsonBranch, Branch.class);
    } catch (Exception e) {
      branch = null;
      e.printStackTrace();
    }
    return branch;
  }
  
  public static Branch getInstance(Document document) {
    Branch branch = Branch.getInstance(document.toJson());
    return branch;
  }
  
  public String getId() {
    return _id.toHexString();
  }
  
  public void setId(String id) {
    this._id = new ObjectId(id);
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getAddress() {
    return address;
  }
  
  public void setAddress(String address) {
    this.address = address;
  }
  
  public String getContactNumber() {
    return contactNumber;
  }
  
  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }
  
  public ArrayList<Executive> getExecutives() {
    return executives;
  }
  
  public void setExecutives(ArrayList<Executive> executives) {
    this.executives = executives;
  }
  
  public void addExecutive(Executive executive) {
    if (this.executives == null) {
      this.executives = new ArrayList<>();
    }
    this.executives.add(executive);
  }
  
  @Override
  public String toString() {
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .create();
    
    return gson.toJson(this);
  }
  
  public String getAlias() {
    return alias;
  }
  
  public void setAlias(String alias) {
    this.alias = alias;
  }
  
  public boolean validateDetails() {
    //ToDo: Validate every detail
    return true;
  }
  
  public Document toDocument() {
    Document document = new Document();
    
    HashMap<String, Object> branchMap = new HashMap<>();
    JSONObject branchJson = new JSONObject(this.toString());
    branchJson
        .keySet()
        .forEach(key -> branchMap.put(key, branchJson.get(key)));
    branchMap.put("executives",
        this.executives
            .stream()
            .map(Executive::toDocument)
            .collect(Collectors.toList()));
    
    ObjectId branchId;
    if (branchMap.containsKey("_id") && branchMap.get("_id") != null && !((String) branchMap.get("_id")).isEmpty()) {
      branchId = new ObjectId((String) branchMap.remove("_id"));
    } else {
      branchId = new ObjectId();
    }
    
    branchMap.put("_id", branchId);
    document.putAll(branchMap);
    
    return document;
  }
}
