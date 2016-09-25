package com.dptradeking.model;

import com.dptradeking.util.gsonadapter.ObjectIdAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Creator: vbarad
 * Date: 2016-08-05
 * Project: DP-TradeKING
 */
public class Executive {
  @Expose
  @SerializedName("_id")
  private ObjectId _id;
  @Expose
  @SerializedName("name")
  private String name;
  @Expose
  @SerializedName("designation")
  private String designation;
  @Expose
  @SerializedName("contactNumber")
  private String contactNumber;
  @Expose
  @SerializedName("email")
  private String email;

  public Executive() {
  }
  
  public Executive(String name, String designation, String contactNumber, String email) {
    this._id = new ObjectId();
    this.name = name;
    this.designation = designation;
    this.contactNumber = contactNumber;
    this.email = email;
  }

  public Executive(String id, String name, String designation, String contactNumber, String email) {
    this._id = new ObjectId(id);
    this.name = name;
    this.designation = designation;
    this.contactNumber = contactNumber;
    this.email = email;
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

  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .create();
  
    return gson.toJson(this);
  }
  
  public boolean validateDetails() {
    //ToDo: Implement proper validation
    return true;
  }
  
  public Document toDocument() {
    Document document = new Document();
    
    HashMap<String, Object> executiveMap = new HashMap<>();
    JSONObject executiveJson = new JSONObject(this.toString());
    executiveJson
        .keySet()
        .forEach(key -> executiveMap.put(key, executiveJson.get(key)));
    
    ObjectId executiveId;
    if (executiveMap.containsKey("_id") && executiveMap.get("_id") != null && !((String) executiveMap.get("_id")).isEmpty()) {
      executiveId = new ObjectId((String) executiveMap.remove("_id"));
    } else {
      executiveId = new ObjectId();
    }
    
    executiveMap.put("_id", executiveId);
    document.putAll(executiveMap);
    
    return document;
  }
}
