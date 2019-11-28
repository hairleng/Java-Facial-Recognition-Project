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
public class ControlPerson {
    ConnectDatabase connectdb = new ConnectDatabase();
    
    /**
     * insert a student record in the database
     * @param mod
     */
    public void insert(ModelPerson mod){
        try{
            connectdb.connect();
            PreparedStatement pst = connectdb.conn.prepareStatement("INSERT INTO info (id, first_name, last_name, dob, program, gender) "
                    + "VALUES(?,?,?,?,?,?)");
            pst.setInt(1, mod.getId());
            pst.setString(2, mod.getFirst_name());
            pst.setString(3, mod.getLast_name());
            pst.setString(4, mod.getDob());
            pst.setString(5, mod.getProgram());
            pst.setString(6, mod.getGender());
            pst.executeUpdate(); // used for adding a record into the database
            System.out.println("has inserted a piece of info which belongs to " + mod.getFirst_name());
        }catch(Exception e){
            System.out.println("Error: " + e);
        }
    }
}
