/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.sql.*;

/**
 *
 * @author Ann
 */
public class ConnectDatabase {
    public Statement stm;
    public ResultSet rs;
    
    public Connection conn;
    //set up a database : mysql database: access_record 
    private final String root = "jdbc:mysql://127.0.0.1/access_record?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private final String driver = "com.mysql.cj.jdbc.Driver";
    private final String user = "root";
    private final String pass = "lwb19961117";
    
    /**
     * connect database
     */
    public void connect(){
        try{
            System.setProperty("jdbc.Driver",driver);
            conn = DriverManager.getConnection (root, user, pass);

        }catch (SQLException e) {
          System.out.println("SQL Exception: " + e);
        }
    }

    /**
     * disconnect database
     */
    public void disconnect(){
        try{
            conn.close();
        }catch(SQLException e){
            System.out.println("disconnect Exception: " + e);
        }
    }
    
    /**
     * create statement and resultset object, execute select query
     * @param SQL
     */
    public void executeSQL(String SQL){
        try{
            stm = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(SQL); // used for select certain record
        }catch(Exception e){
            System.out.println("execute Exception: " + e);
        }
    }
}
