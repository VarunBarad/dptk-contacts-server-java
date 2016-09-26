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
public class Department {
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
  @SerializedName("executives")
  private ArrayList<Executive> executives;

  public Department() {
  }
  
  public Department(String name, String alias) {
    this._id = new ObjectId();
    this.name = name;
    this.alias = alias;
    this.executives = new ArrayList<>();
  }
  
  public Department(String id, String name, String alias, ArrayList<Executive> executives) {
    this._id = new ObjectId(id);
    this.name = name;
    this.alias = alias;
    if (executives != null) {
      this.executives = executives;
    } else {
      this.executives = new ArrayList<>();
    }
  }
  
  public static Department getInstance(String jsonDepartment) {
    Gson gson = new Gson();
    Department department;
    try {
      department = gson.fromJson(jsonDepartment, Department.class);
    } catch (Exception e) {
      department = null;
      e.printStackTrace();
    }
    return department;
  }
  
  public static Department getInstance(Document document) {
    Department department = Department.getInstance(document.toJson());
    return department;
  }
  
  public static boolean validateName(String name) {
    boolean isValid;
    
    isValid = !(name == null || name.isEmpty());
    
    return isValid;
  }
  
  public static boolean validateAlias(String alias) {
    boolean isValid;
    
    isValid = !(alias == null || alias.isEmpty());
    
    return isValid;
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

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
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
  
  public boolean validateDetails() {
    boolean isExecutivesValid =
        this.executives
            .stream()
            .reduce(true, (isValid, executive) -> isValid && executive.validateDetails(), (b1, b2) -> b1 && b2);
  
    return isExecutivesValid &&
        Department.validateName(this.name) &&
        Department.validateAlias(this.alias);
  }
  
  public Document toDocument() {
    Document document = new Document();
    
    HashMap<String, Object> departmentMap = new HashMap<>();
    JSONObject departmentJson = new JSONObject(this.toString());
    departmentJson
        .keySet()
        .forEach(key -> departmentMap.put(key, departmentJson.get(key)));
    departmentMap.put("executives",
        this.executives
            .stream()
            .map(Executive::toDocument)
            .collect(Collectors.toList()));
    
    ObjectId departmentId;
    if (departmentMap.containsKey("_id") && departmentMap.get("_id") != null && !((String) departmentMap.get("_id")).isEmpty()) {
      departmentId = new ObjectId((String) departmentMap.remove("_id"));
    } else {
      departmentId = new ObjectId();
    }
    
    departmentMap.put("_id", departmentId);
    document.putAll(departmentMap);
    
    return document;
  }
}
