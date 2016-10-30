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
      if (mainWorkbookHelper != null) {
        databaseHelper.clearDatabase();
      
        ArrayList<SubBroker> subBrokers = mainWorkbookHelper.getSubBrokers();
        databaseHelper.insertMultipleSubBrokers(subBrokers);
      
        ArrayList<Department> departments = mainWorkbookHelper.getDepartments();
        DepartmentsWorkbookHelper departmentsWorkbookHelper = DepartmentsWorkbookHelper.getInstance(new File(Config.Workbook.FILE_HEAD_OFFICE), departments);
        departments = new ArrayList<>(departmentsWorkbookHelper.getFilledDepartments());
        departmentsWorkbookHelper.close();
        databaseHelper.insertMultipleDepartments(departments);
      
        ArrayList<Branch> branches = mainWorkbookHelper.getBranches();
        BranchesWorkbookHelper branchesWorkbookHelper = BranchesWorkbookHelper.getInstance(new File(Config.Workbook.FILE_BRANCHES), branches);
        branches = new ArrayList<>(branchesWorkbookHelper.getFilledBranches());
        branchesWorkbookHelper.close();
        databaseHelper.insertMultipleBranches(branches);
      
        mainWorkbookHelper.close();
      
        PrintWriter writer = response.getWriter();
        writer.write("{\"status\": \"200\", \"message\": \"Database Refreshed\"}");
        writer.close();
      } else {
        System.err.println("File doesn\'t exist");
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
      
      PrintWriter writer = response.getWriter();
      writer.write("{\"status\": \"500\", \"message\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}");
      writer.close();
    }
  }
}
