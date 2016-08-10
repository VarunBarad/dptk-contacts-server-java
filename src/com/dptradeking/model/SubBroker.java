package com.dptradeking.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.bson.types.ObjectId;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
  private String id;
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

  public SubBroker(String id, String name, String address, String contactNumber, String email, String registrationNumber, String incorporationDate) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.contactNumber = contactNumber;
    this.email = email;
    this.registrationNumber = registrationNumber;
    this.incorporationDate = incorporationDate;
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

  public static boolean validateName(String name) {
    boolean isValid;

    if (name == null || name.isEmpty()) {
      isValid = false;
    } else {
      isValid = true;
    }

    return isValid;
  }

  public static boolean validateAddress(String address) {
    boolean isValid;

    if (address == null || address.isEmpty()) {
      isValid = false;
    } else {
      isValid = true;
    }

    return isValid;
  }

  public static boolean validateContactNumber(String contactNumber) {
    boolean isValid;

    if (contactNumber == null || contactNumber.isEmpty() || !contactNumber.matches("^(\\+91)?[1-9][0-9]{9}$")) {
      isValid = false;
    } else {
      isValid = true;
    }

    return isValid;
  }

  public static boolean validateEmail(String email) {
    boolean isValid;

    //ToDo: Check email regex
    if (email == null || email.isEmpty()) {
      isValid = false;
    } else {
      isValid = true;
    }

    return isValid;
  }

  public static boolean validateRegistrationNumber(String registrationNumber) {
    boolean isValid;

    //ToDo: Check registration number regex
    if (registrationNumber == null || registrationNumber.isEmpty()) {
      isValid = false;
    } else {
      isValid = true;
    }

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
        if (currentDate.before(date)) {
          isValid = false;
        } else {
          isValid = true;
        }
      } catch (ParseException e) {
        e.printStackTrace();
        isValid = false;
      }
    }

    return isValid;
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

  public String getIncorporationDate() {
    return incorporationDate;
  }

  public void setIncorporationDate(String incorporationDate) {
    this.incorporationDate = incorporationDate;
  }
}
