package com.dptradeking.model;

import com.dptradeking.util.gsonadapter.ObjectIdAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Creator: vbarad
 * Date: 2016-08-05
 * Project: DP-TradeKING
 */
public class SubBroker {
  @Expose
  @SerializedName("_id")
  private ObjectId id;
  @Expose
  @SerializedName("name")
  private String name;
  @Expose
  @SerializedName("address")
  private String address;
  @Expose
  @SerializedName("contactNumber")
  private String contactNumber;
  @Expose
  @SerializedName("email")
  private String email;
  @Expose
  @SerializedName("registrationNumber")
  private String registrationNumber;
  @Expose
  @SerializedName("incorporationDate")
  private String incorporationDate;
  
  public SubBroker() {
  }
  
  public SubBroker(String name, String address, String contactNumber, String email, String registrationNumber, String incorporationDate) {
    this.id = new ObjectId();
    this.name = name;
    this.address = address;
    this.contactNumber = contactNumber;
    this.email = email;
    this.registrationNumber = registrationNumber;
    this.incorporationDate = incorporationDate;
  }
  
  public SubBroker(String id, String name, String address, String contactNumber, String email, String registrationNumber, String incorporationDate) {
    this.id = new ObjectId(id);
    this.name = name;
    this.address = address;
    this.contactNumber = contactNumber;
    this.email = email;
    this.registrationNumber = registrationNumber;
    this.incorporationDate = incorporationDate;
  }
  
  public static SubBroker getInstance(String jsonSubBroker) {
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .create();
    SubBroker subBroker;
    try {
      subBroker = gson.fromJson(jsonSubBroker, SubBroker.class);
    } catch (JsonSyntaxException e) {
      subBroker = null;
      e.printStackTrace();
    }
    return subBroker;
  }
  
  public static SubBroker getInstance(Document document) {
    JSONObject documentJson = new JSONObject(document.toJson());
    
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .create();
    SubBroker subBroker;
    
    try {
      subBroker = gson.fromJson(documentJson.toString(), SubBroker.class);
    } catch (JsonSyntaxException e) {
      subBroker = null;
      e.printStackTrace();
    }
    
    return subBroker;
  }
  
  public static boolean validateName(String name) {
    boolean isValid;
    
    isValid = !(name == null || name.isEmpty());
    
    return isValid;
  }
  
  public static boolean validateAddress(String address) {
    boolean isValid;
    
    isValid = !(address == null || address.isEmpty());
    
    return isValid;
  }
  
  public static boolean validateContactNumber(String contactNumber) {
    boolean isValid;
    
    isValid = !(contactNumber == null || contactNumber.isEmpty() || !contactNumber.matches("^(\\+91)?[1-9][0-9]{9}$"));
    
    return isValid;
  }
  
  public static boolean validateEmail(String email) {
    boolean isValid;
    
    //ToDo: Check email regex
    isValid = !(email == null || email.isEmpty());
    
    return isValid;
  }
  
  public static boolean validateRegistrationNumber(String registrationNumber) {
    boolean isValid;
    
    //ToDo: Check registration number regex
    isValid = !(registrationNumber == null || registrationNumber.isEmpty());
    
    return isValid;
  }
  
  public static boolean validateIncorporationDate(String incorporationDate) {
    boolean isValid;
    
    if (incorporationDate == null || incorporationDate.isEmpty()) {
      isValid = false;
    } else {
      try {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(incorporationDate);
        Date currentDate = new Date();
        isValid = !currentDate.before(date);
      } catch (ParseException e) {
        e.printStackTrace();
        isValid = false;
      }
    }
    
    return isValid;
  }
  
  public String getId() {
    return id.toHexString();
  }
  
  public void setId(String id) {
    this.id = new ObjectId(id);
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
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getRegistrationNumber() {
    return registrationNumber;
  }
  
  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }
  
  public String getIncorporationDate() {
    return incorporationDate;
  }
  
  public void setIncorporationDate(String incorporationDate) {
    this.incorporationDate = incorporationDate;
  }
  
  @Override
  public String toString() {
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .create();
    
    return gson.toJson(this);
  }
  
  public Document toDocument() {
    Document document = new Document();
    
    HashMap<String, Object> subBrokerMap = new HashMap<>();
    JSONObject subBrokerJson = new JSONObject(this.toString());
    subBrokerJson
        .keySet()
        .forEach(key -> subBrokerMap.put(key, subBrokerJson.get(key)));
    
    ObjectId subBrokerId;
    if (subBrokerMap.containsKey("_id") && subBrokerMap.get("_id") != null && !((String) subBrokerMap.get("_id")).isEmpty()) {
      subBrokerId = new ObjectId((String) subBrokerMap.remove("_id"));
    } else {
      subBrokerId = new ObjectId();
    }
    
    subBrokerMap.put("_id", subBrokerId);
    document.putAll(subBrokerMap);
    
    return document;
  }
  
  public boolean validateDetails() {
    return SubBroker.validateName(this.name) &&
        SubBroker.validateAddress(this.address) &&
        SubBroker.validateContactNumber(this.contactNumber) &&
        SubBroker.validateEmail(this.email) &&
        SubBroker.validateRegistrationNumber(this.registrationNumber) &&
        SubBroker.validateIncorporationDate(this.incorporationDate);
  }
}
