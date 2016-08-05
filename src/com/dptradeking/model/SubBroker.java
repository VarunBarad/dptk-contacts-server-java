package com.dptradeking.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.bson.types.ObjectId;

/**
 * Creator: vbarad
 * Date: 2016-08-05
 * Project: DP-TradeKING
 */
public class SubBroker {
  @SerializedName("_id")
  private ObjectId _id;
  @Expose
  @SerializedName("id")
  private String id = "";
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

  public SubBroker() {
  }

  public SubBroker(String id, String name, String address, String contactNumber, String email, String registrationNumber) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.contactNumber = contactNumber;
    this.email = email;
    this.registrationNumber = registrationNumber;
  }

  public static SubBroker SubBrokerFactory(String jsonSubBroker) {
    Gson gson = new Gson();
    SubBroker subBroker;
    try {
      subBroker = gson.fromJson(jsonSubBroker, SubBroker.class);
    } catch (Exception e) {
      subBroker = null;
      e.printStackTrace();
    }
    return subBroker;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  @Override
  public String toString() {
    return ((new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()).toJson(this));
  }

  public void populateId() {
    this.id = this._id.toHexString();
  }
}
