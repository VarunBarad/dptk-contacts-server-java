package com.dptradeking.model;

import com.dptradeking.util.gsonadapter.ObjectIdAdapter;
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
public class HeadOfDepartment {
  @Expose
  @SerializedName("_id")
  private ObjectId _id;
  @Expose
  @SerializedName("name")
  private String name;
  @Expose
  @SerializedName("contactNumber")
  private String contactNumber;
  @Expose
  @SerializedName("email")
  private String email;

  public HeadOfDepartment() {
  }

  public HeadOfDepartment(String id, String name, String contactNumber, String email) {
    this._id = new ObjectId(id);
    this.name = name;
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
}
