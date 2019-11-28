/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

/**
 *
 * @author Ann
 */

// class ModelPerson is the data structure of student object, have all the student personal info
public class ModelPerson {
    private int id;
    private String first_name;
    private String last_name;
    private String dob;
    private String program;
    private String gender;
    
    public ModelPerson(){
        
    }
    
    public ModelPerson(int id, String first_name, String last_name, String dob, String program, String gender){
         this.id = id;
         this.first_name = first_name;
         this.last_name = last_name;
         this.dob = dob;
         this.program = program;
         this.gender = gender;
    }

    //getter and setter for each field
    
    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getDob() {
        return dob;
    }

    public String getProgram() {
        return program;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setProgram(String program) {
        this.program = program;
    }
}
