package com.dptradeking.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.bson.types.ObjectId;

import java.util.ArrayList;

/**
 * Creator: vbarad
 * Date: 2016-08-05
 * Project: DP-TradeKING
 */
public class Branch {
  @SerializedName("_id")
  private ObjectId _id;
  @Expose
  @SerializedName("id")
  private String id;
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
  @SerializedName("branchManager")
  private BranchManager branchManager;
  @Expose
  @SerializedName("executives")
  private ArrayList<Executive> executives;

  public Branch() {
    this.executives = new ArrayList<>();
  }

  public Branch(String id, String name, String alias, String address, String contactNumber, BranchManager branchManager) {
    this.id = id;
    this.name = name;
    this.alias = alias;
    this.address = address;
    this.contactNumber = contactNumber;
    this.branchManager = branchManager;
  }

  public static Branch BranchFactory(String jsonBranch) {
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

  public BranchManager getBranchManager() {
    return branchManager;
  }

  public void setBranchManager(BranchManager branchManager) {
    this.branchManager = branchManager;
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
    return ((new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()).toJson(this));
  }

  public void populateId() {
    this.id = this._id.toHexString();
    this.branchManager.populateId();
    for (int i = 0; i < this.getExecutives().size(); i++) {
      this.getExecutives().get(i).populateId();
    }
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }
}
