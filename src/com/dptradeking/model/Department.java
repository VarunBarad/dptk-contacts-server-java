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
public class Department {
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
  @SerializedName("headOfDepartment")
  private HeadOfDepartment headOfDepartment;
  @Expose
  @SerializedName("executives")
  private ArrayList<Executive> executives;

  public Department() {
  }

  public Department(String id, String name, String alias, HeadOfDepartment headOfDepartment, ArrayList<Executive> executives) {
    this.id = id;
    this.name = name;
    this.alias = alias;
    this.headOfDepartment = headOfDepartment;
    this.executives = executives;
  }

  public static Department DepartmentFactory(String jsonDepartment) {
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

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public HeadOfDepartment getHeadOfDepartment() {
    return headOfDepartment;
  }

  public void setHeadOfDepartment(HeadOfDepartment headOfDepartment) {
    this.headOfDepartment = headOfDepartment;
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
    this.headOfDepartment.populateId();
    for (int i = 0; i < this.getExecutives().size(); i++) {
      this.getExecutives().get(i).populateId();
    }
  }
}
