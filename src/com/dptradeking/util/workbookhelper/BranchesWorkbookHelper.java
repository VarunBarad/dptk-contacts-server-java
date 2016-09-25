package com.dptradeking.util.workbookhelper;

import com.dptradeking.model.Branch;
import com.dptradeking.model.Executive;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * Creator: vbarad
 * Date: 2016-09-25
 * Project: DP-TradeKING
 */
public class BranchesWorkbookHelper {
  private FileInputStream fileInputStream;
  private XSSFWorkbook workbook;
  private ArrayList<Branch> branches;
  
  private BranchesWorkbookHelper(File workbookFile, ArrayList<Branch> branches) {
    try {
      this.fileInputStream = new FileInputStream(workbookFile);
      this.workbook = new XSSFWorkbook(this.fileInputStream);
      this.branches = branches;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static BranchesWorkbookHelper getInstance(File workbookFile, ArrayList<Branch> branches) {
    BranchesWorkbookHelper branchesWorkbookHelper;
    
    if (workbookFile.exists()) {
      branchesWorkbookHelper = new BranchesWorkbookHelper(workbookFile, branches);
    } else {
      branchesWorkbookHelper = null;
    }
    
    return branchesWorkbookHelper;
  }
  
  public List<Branch> getFilledBranches() {
    return this.branches
        .stream()
        .map(branch -> {
          branch.setExecutives(this.getExecutives(branch.getName()));
          return branch;
        }).collect(Collectors.toList());
  }
  
  private ArrayList<Executive> getExecutives(String branchName) {
    XSSFSheet executivesSheet = this.workbook.getSheet(branchName);
    ArrayList<Executive> executives;
    
    if (executivesSheet != null) {
      HashMap<String, Integer> titles = this.getTitles(executivesSheet);
      executives = new ArrayList<>();
      
      Iterator<Row> rowIterator = executivesSheet.rowIterator();
      rowIterator.next(); //Skip the titles row
      
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        
        try {
          Executive executive = new Executive(
              row.getCell(titles.get("name")).getStringCellValue(),
              row.getCell(titles.get("designation")).getStringCellValue(),
              row.getCell(titles.get("contactNumber")).getStringCellValue(),
              row.getCell(titles.get("email")).getStringCellValue()
          );
          
          if (executive.validateDetails()) {
            executives.add(executive);
          }
        } catch (NullPointerException e) {
          e.printStackTrace();
          //ToDo: Raise proper exception here
        }
      }
    } else {
      executives = null;
      //ToDo: Raise proper exception here
    }
    
    return executives;
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
