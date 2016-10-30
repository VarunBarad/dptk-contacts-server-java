package com.dptradeking.refresh;

import com.dptradeking.config.Config;
import com.dptradeking.model.Branch;
import com.dptradeking.model.Department;
import com.dptradeking.model.SubBroker;
import com.dptradeking.util.DatabaseHelper;
import com.dptradeking.util.workbookhelper.BranchesWorkbookHelper;
import com.dptradeking.util.workbookhelper.DepartmentsWorkbookHelper;
import com.dptradeking.util.workbookhelper.MainWorkbookHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Creator: vbarad
 * Date: 2016-09-21
 * Project: DP-TradeKING
 */
@WebServlet(name = "RefreshServlet")
public class RefreshServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    DatabaseHelper databaseHelper = new DatabaseHelper(
        Config.Database.DB_HOST,
        Config.Database.DB_NAME
    );
  
    MainWorkbookHelper mainWorkbookHelper = MainWorkbookHelper.getInstance(new File(Config.Workbook.FILE_MAIN));
  
    try {
      if (mainWorkbookHelper == null) {
        throw new NullPointerException("Missing \"main.xlsx.\" file.\nPlease make sure that you have the details of all the Head-Office departments, Branches and Sub-Brokers inside a file named \"main.xlsx\".\nEven when you don't have data for some of the categories, keep a blank sheet but do have a file of given name.");
      }
    
      databaseHelper.clearDatabase();
    
      ArrayList<SubBroker> subBrokers = mainWorkbookHelper.getSubBrokers();
      databaseHelper.insertMultipleSubBrokers(subBrokers);
    
      ArrayList<Department> departments = mainWorkbookHelper.getDepartments();
      DepartmentsWorkbookHelper departmentsWorkbookHelper = DepartmentsWorkbookHelper.getInstance(new File(Config.Workbook.FILE_HEAD_OFFICE), departments);
      if (departmentsWorkbookHelper == null) {
        throw new NullPointerException("Missing \"headOffice.xlsx.\" file.\nPlease make sure that you have the details of all executives of all the departments of Head-Office inside a file named \"headOffice.xlsx\".\nEven when you don't have data for any departments, keep a blank file but do have a file of given name.");
      }
      departments = new ArrayList<>(departmentsWorkbookHelper.getFilledDepartments());
      departmentsWorkbookHelper.close();
      databaseHelper.insertMultipleDepartments(departments);
    
      ArrayList<Branch> branches = mainWorkbookHelper.getBranches();
      BranchesWorkbookHelper branchesWorkbookHelper = BranchesWorkbookHelper.getInstance(new File(Config.Workbook.FILE_BRANCHES), branches);
      if (branchesWorkbookHelper == null) {
        throw new NullPointerException("Missing \"branches.xlsx.\" file.\nPlease make sure that you have the details of all executives of all the branches inside a file named \"branches.xlsx\".\nEven when you don't have data for any branches, keep a blank file but do have a file of given name.");
      }
      branches = new ArrayList<>(branchesWorkbookHelper.getFilledBranches());
      branchesWorkbookHelper.close();
      databaseHelper.insertMultipleBranches(branches);
    
      mainWorkbookHelper.close();
    
      PrintWriter writer = response.getWriter();
      writer.write("{\"status\": \"200\", \"message\": \"Database Refreshed\"}");
      writer.close();
    } catch (NullPointerException e) {
      e.printStackTrace();
      
      PrintWriter writer = response.getWriter();
      writer.write("{\"status\": \"500\", \"message\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}");
      writer.close();
    }
  }
}
