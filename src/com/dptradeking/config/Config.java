package com.dptradeking.config;

/**
 * Creator: vbarad
 * Date: 2016-09-25
 * Project: DP-TradeKING
 */
public class Config {
  public static final String AUTH_PASS_HASH = System.getenv("AUTH_PASS_HASH");
  
  public static class Database {
    public static final String DB_HOST = "localhost";
    public static final String DB_NAME = "dptkContacts";
    public static final String DB_USER = System.getenv("DB_USERNAME");
    public static final String DB_PASS = System.getenv("DB_PASSWORD");
  }
  
  public static class Workbook {
    public static final String FILE_MAIN = "/root/contacts/main.xlsx";
    public static final String FILE_HEAD_OFFICE = "/root/contacts/headOffice.xlsx";
    public static final String FILE_BRANCHES = "/root/contacts/branches.xlsx";
  }
}
