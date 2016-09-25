package com.dptradeking.util.workbookhelper;

import com.dptradeking.model.Branch;
import com.dptradeking.model.Department;
import com.dptradeking.model.SubBroker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Creator: vbarad
 * Date: 2016-09-20
 * Project: DP-TradeKING
 */
public class MainWorkbookHelper {
  private FileInputStream fileInputStream;
  private XSSFWorkbook workbook;
  
  private MainWorkbookHelper(File workbookFile) {
    try {
      this.fileInputStream = new FileInputStream(workbookFile);
      this.workbook = new XSSFWorkbook(this.fileInputStream);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static MainWorkbookHelper getInstance(File workbookFile) {
    MainWorkbookHelper mainWorkbookHelper;
    
    if (workbookFile.exists()) {
      mainWorkbookHelper = new MainWorkbookHelper(workbookFile);
    } else {
      mainWorkbookHelper = null;
    }
    
    return mainWorkbookHelper;
  }
  
  public ArrayList<Department> getDepartments() {
    XSSFSheet departmentsSheet = this.workbook.getSheet("Head-Office");
    ArrayList<Department> departments;
    
    if (departmentsSheet != null) {
      HashMap<String, Integer> titles = this.getTitles(departmentsSheet);
      departments = new ArrayList<>();
      
      Iterator<Row> rowIterator = departmentsSheet.rowIterator();
      rowIterator.next(); //Skip the titles row
      
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        
        try {
          Department d = new Department(
              row.getCell(titles.get("name")).getStringCellValue(),
              row.getCell(titles.get("alias")).getStringCellValue()
          );
          
          if (d.validateDetails()) {
            departments.add(d);
          }
        } catch (NullPointerException e) {
          e.printStackTrace();
          //ToDo: Raise proper exception here
        }
      }
    } else {
      departments = null;
      //ToDo: Raise a proper exception here
    }
    
    return departments;
  }
  
  public ArrayList<Branch> getBranches() {
    XSSFSheet branchesSheet = this.workbook.getSheet("Branches");
    ArrayList<Branch> branches;
    
    if (branchesSheet != null) {
      HashMap<String, Integer> titles = this.getTitles(branchesSheet);
      branches = new ArrayList<>();
      
      Iterator<Row> rowIterator = branchesSheet.rowIterator();
      rowIterator.next(); //Skip the titles row
      
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        
        try {
          Branch b = new Branch(
              row.getCell(titles.get("name")).getStringCellValue(),
              row.getCell(titles.get("alias")).getStringCellValue(),
              row.getCell(titles.get("address")).getStringCellValue(),
              row.getCell(titles.get("contactNumber")).getStringCellValue()
          );
          
          if (b.validateDetails()) {
            branches.add(b);
          }
        } catch (NullPointerException e) {
          e.printStackTrace();
          //ToDo: Raise proper exception here
        }
      }
    } else {
      branches = null;
      //ToDo: Raise a proper exception here
    }
    
    return branches;
  }
  
  public ArrayList<SubBroker> getSubBrokers() {
    XSSFSheet subBrokersSheet = this.workbook.getSheet("Sub-Brokers");
    ArrayList<SubBroker> subBrokers;
    
    if (subBrokersSheet != null) {
      HashMap<String, Integer> titles = this.getTitles(subBrokersSheet);
      subBrokers = new ArrayList<>();
      
      Iterator<Row> rowIterator = subBrokersSheet.rowIterator();
      rowIterator.next(); //Skip the titles row
      
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        
        try {
          SubBroker s = new SubBroker(
              row.getCell(titles.get("name")).getStringCellValue(),
              row.getCell(titles.get("address")).getStringCellValue(),
              row.getCell(titles.get("contactNumber")).getStringCellValue(),
              row.getCell(titles.get("email")).getStringCellValue(),
              row.getCell(titles.get("registrationNumber")).getStringCellValue(),
              row.getCell(titles.get("incorporationDate")).getStringCellValue()
          );
          
          if (s.validateDetails()) {
            subBrokers.add(s);
          }
        } catch (NullPointerException e) {
          e.printStackTrace();
          //ToDo: Raise a proper exception here
        }
      }
    } else {
      subBrokers = null;
      //ToDo: Raise proper exception here
    }
    
    return subBrokers;
  }
  
  private HashMap<String, Integer> getTitles(XSSFSheet sheet) {
    HashMap<String, Integer> titles;
    Row titlesRow = sheet.getRow(0);
    if (titlesRow != null) {
      titles = new HashMap<>();
      
      Iterator<Cell> cellIterator = titlesRow.cellIterator();
      for (int i = 0; cellIterator.hasNext(); i++) {
        String cellValue = cellIterator.next().getStringCellValue();
        titles.put(cellValue, i);
      }
    } else {
      titles = null;
      //ToDo: Raise a proper exception here
    }
    
    return titles;
  }
  
  public void close() {
    try {
      this.fileInputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
