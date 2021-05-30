package com.example.juiceup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class DatabaseConnection {

    private static DatabaseConnection instance = new DatabaseConnection();

    private Integer a;
    private DatabaseConnection(){
        this.a = 10;
    }


    public static DatabaseConnection getInstance(){
        return instance;
    }

    public Integer test(){
        return a;
    }

    public void set_int(Integer new_value){
        this.a = new_value;
    }


}
