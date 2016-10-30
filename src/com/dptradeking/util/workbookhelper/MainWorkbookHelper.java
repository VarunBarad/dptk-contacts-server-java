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
  
      // Check whether all the required columns are present or not
      if (!titles.containsKey("name")) {
        throw new NullPointerException("The name of the department must come under a column titled \"name\" in the sheet named \"Head-Office\".\nIn case if you don't want to have that information, keep an empty cell under the column containing that title.");
      }
      if (!titles.containsKey("alias")) {
        throw new NullPointerException("The alias of the department must come under a column titled \"alias\" in the sheet named \"Head-Office\".\nIn case if you don't want to have that information, keep an empty cell under the column containing that title.");
      }
      
      departments = new ArrayList<>();
      
      Iterator<Row> rowIterator = departmentsSheet.rowIterator();
      rowIterator.next(); //Skip the titles row
      
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
  
        Department d = new Department(
            row.getCell(titles.get("name")).getStringCellValue(),
            row.getCell(titles.get("alias")).getStringCellValue()
        );
  
        if (d.validateDetails()) {
          departments.add(d);
        } else {
          throw new NullPointerException("Invalid details for the department in row " + (row.getRowNum() + 1) + " of sheet \"Head-Office\" in file \"main.xlsx\".");
        }
      }
    } else {
      departments = null;
      throw new NullPointerException("The details of departments of Head-Office needs to go in a worksheet named \"Head-Office\" inside the file \"main.xlsx\".\nIn the case when you don't have any departments keep an empty sheet by the same name with appropriate column headers.");
    }
    
    return departments;
  }
  
  public ArrayList<Branch> getBranches() throws NullPointerException {
    XSSFSheet branchesSheet = this.workbook.getSheet("Branches");
    ArrayList<Branch> branches;
    
    if (branchesSheet != null) {
      HashMap<String, Integer> titles = this.getTitles(branchesSheet);
  
      // Check whether all the required columns are present or not
      if (!titles.containsKey("name")) {
        throw new NullPointerException("The name of the branch must come under a column titled \"name\" in the sheet named \"Branches\".\nIn case if you don't want to have that information, keep an empty cell under the column containing that title.");
      }
      if (!titles.containsKey("alias")) {
        throw new NullPointerException("The alias of the branch must come under a column titled \"alias\" in the sheet named \"Branches\".\nIn case if you don't want to have that information, keep an empty cell under the column containing that title.");
      }
      if (!titles.containsKey("address")) {
        throw new NullPointerException("The address of the branch must come under a column titled \"address\" in the sheet named \"Branches\".\nIn case if you don't want to have that information, keep an empty cell under the column containing that title.");
      }
      if (!titles.containsKey("contactNumber")) {
        throw new NullPointerException("The contact-number of the branch must come under a column titled \"contactNumber\" in the sheet named \"Branches\".\nIn case if you don't want to have that information, keep an empty cell under the column containing that title.");
      }
    
      branches = new ArrayList<>();
      
      Iterator<Row> rowIterator = branchesSheet.rowIterator();
      rowIterator.next(); //Skip the titles row
      
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
  
        Branch b = new Branch(
            row.getCell(titles.get("name")).getStringCellValue(),
            row.getCell(titles.get("alias")).getStringCellValue(),
            row.getCell(titles.get("address")).getStringCellValue(),
            row.getCell(titles.get("contactNumber")).getStringCellValue()
        );
  
        if (b.validateDetails()) {
          branches.add(b);
        } else {
          throw new NullPointerException("Invalid details for the branch in row " + (row.getRowNum() + 1) + " of sheet \"Branches\" in file \"main.xlsx\".");
        }
      }
    } else {
      branches = null;
      throw new NullPointerException("The details of branches needs to go in a worksheet named \"Branches\" inside the file \"main.xlsx\".\nIn the case when you don't have any branches keep an empty sheet by the same name.");
    }
    
    return branches;
  }
  
  public ArrayList<SubBroker> getSubBrokers() throws NullPointerException {
    XSSFSheet subBrokersSheet = this.workbook.getSheet("Sub-Brokers");
    ArrayList<SubBroker> subBrokers;
    
    if (subBrokersSheet != null) {
      HashMap<String, Integer> titles = this.getTitles(subBrokersSheet);
  
      // Check whether all the columns required are present or not
      if (!titles.containsKey("name")) {
        throw new NullPointerException("The name of the sub-broker must come under a column titled \"name\" in the sheet named \"Sub-Brokers\".\nIn case if you don't want to have that information, keep an empty cell under the column containing that title.");
      }
      if (!titles.containsKey("address")) {
        throw new NullPointerException("The address of the sub-broker must come under a column titled \"address\" in the sheet named \"Sub-Brokers\".\nIn case if you don't want to have that information, keep an empty cell under the column containing that title.");
      }
      if (!titles.containsKey("contactNumber")) {
        throw new NullPointerException("The contact-number of the sub-broker must come under a column titled \"contactNumber\" in the sheet named \"Sub-Brokers\".\nIn case if you don't want to have that information, keep an empty cell under the column containing that title.");
      }
      if (!titles.containsKey("email")) {
        throw new NullPointerException("The email of the sub-broker must come under a column titled \"email\" in the sheet named \"Sub-Brokers\".\nIn case if you don't want to have that information, keep an empty cell under the column containing that title.");
      }
      if (!titles.containsKey("registrationNumber")) {
        throw new NullPointerException("The registration-number of the sub-broker must come under a column titled \"registrationNumber\" in the sheet named \"Sub-Brokers\".\nIn case if you don't want to have that information, keep an empty cell under the column containing that title.");
      }
      if (!titles.containsKey("incorporationDate")) {
        throw new NullPointerException("The incorporation-date of the sub-broker must come under a column titled \"incorporationDate\" in the sheet named \"Sub-Brokers\".\nIn case if you don't want to have that information, keep an empty cell under the column containing that title.");
      }
      
      subBrokers = new ArrayList<>();
      
      Iterator<Row> rowIterator = subBrokersSheet.rowIterator();
      rowIterator.next(); //Skip the titles row
      
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
  
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
        } else {
          throw new NullPointerException("Invalid details for the sub-broker in row " + (row.getRowNum() + 1) + " of sheet \"Sub-Brokers\" in file \"main.xlsx\".");
        }
      }
    } else {
      subBrokers = null;
      throw new NullPointerException("The details of sub-brokers needs to go in a worksheet named \"Sub-Brokers\".\nIn the case when you don't have any sub-brokers keep an empty sheet by the same name.");
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
