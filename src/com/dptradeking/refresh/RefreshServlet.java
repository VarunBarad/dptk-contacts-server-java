package com.dptradeking.refresh;

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
        this.getServletContext().getInitParameter("database-host"),
        this.getServletContext().getInitParameter("database-name")
    );
    MainWorkbookHelper workbookHelper = MainWorkbookHelper.getInstance(new File("/home/vbarad/Documents/Workspace/Projects/DP Trade King/excel-files/main.xlsx"));
    
    if (workbookHelper != null) {
      databaseHelper.clearDatabase();
      
      ArrayList<SubBroker> subBrokers = workbookHelper.getSubBrokers();
      databaseHelper.insertMultipleSubBrokers(subBrokers);
      
      ArrayList<Department> departments = workbookHelper.getDepartments();
      DepartmentsWorkbookHelper departmentsWorkbookHelper = DepartmentsWorkbookHelper.getInstance(new File("/home/vbarad/Documents/Workspace/Projects/DP Trade King/excel-files/headOffice.xlsx"), departments);
      departments = new ArrayList<>(departmentsWorkbookHelper.getFilledDepartments());
      databaseHelper.insertMultipleDepartments(departments);
      
      ArrayList<Branch> branches = workbookHelper.getBranches();
      BranchesWorkbookHelper branchesWorkbookHelper = BranchesWorkbookHelper.getInstance(new File("/home/vbarad/Documents/Workspace/Projects/DP Trade King/excel-files/branches.xlsx"), branches);
      branches = new ArrayList<>(branchesWorkbookHelper.getFilledBranches());
      databaseHelper.insertMultipleBranches(branches);
      
      PrintWriter writer = response.getWriter();
      writer.write("{\"message\": \"Database Refreshed\"}");
      writer.close();
    } else {
      System.err.println("File doesn\'t exist");
    }
  }
}
