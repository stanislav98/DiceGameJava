/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university.dicegame;

import java.awt.Color;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author User
 */
public class ServiceClass {
    private DataBase db;
    private DiceTupleModel m;
    private View v;
    private final int rolls = 300;
    int arr[] = new int[rolls];

    
    public ServiceClass(DiceTupleModel m,View v) {
        this.db = new DataBase();
        this.m = m;
        this.v = v;
    }
    
    public ResultSet getRollsByDate(java.sql.Date sqlDate) {
        try {
            return db.getRollsByDate(sqlDate);
        } catch(Exception e) {
            System.err.println("Some error occured while getting dates !");
            System.err.println(e.getMessage());
        }
        
        return null;
    }
    
    public void servicePrepareStatement() {
        try {
            db.prepareStatementQuery();
        } catch(Exception e) {
            System.err.println("Some error occured while preparing in service!");
            System.err.println(e.getMessage());
        }
    }
    
    public void servicePreparedStatementKV(int k, int v) {
        try {
            db.prepareStatemntQueryValue(k,v);
        } catch(Exception e) {
            System.err.println("Some error occured while preparing KV Statement in service!");
            System.err.println(e.getMessage());
        }
    }
    
    public void Insert() {
        try {
            db.InsertQuery();
        } catch(Exception e) {
            System.err.println("Some error occured while Inserting in service!");
            System.err.println(e.getMessage());
        }
    }
    
    public ResultSet getDates() {
        try {
            return db.getDatesAvailable();
        } catch(Exception e) {
            System.err.println("Some error occured while getting Dates in service!");
            System.err.println(e.getMessage());
        }
        
        return null;
    }
    
    public void rollDices(Map<Integer, Integer> mp) {
        for(int i = 0; i < rolls ; i++ ) {
            m = new DiceTupleModel();
            arr[i] = m.getTupleSum();
            if (mp.containsKey(arr[i])) {
                mp.put(arr[i], mp.get(arr[i]) + 1);
            } else {
                mp.put(arr[i], 1);
            }
        }
        
        //after row we prepare the db
        this.servicePrepareStatement();
    }
    
}
