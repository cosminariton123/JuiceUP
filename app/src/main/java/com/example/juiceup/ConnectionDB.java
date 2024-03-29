package com.example.juiceup;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    private static ConnectionDB instance = new ConnectionDB();


    private String ip;
    private String port;
    private String Driver;
    private String database;
    private String username;
    private String password;
    private String connectionString;
    private Connection connection;


    private ConnectionDB(){


        ip = "cosmincoco.go.ro";
        port = "1433";
        Driver = "net.sourceforge.jtds.jdbc.Driver";
        database = "juiceupdatabase";
        username = "client";
        password = "123";
        connectionString = "jdbc:jtds:sqlserver://" +ip  + ":" + port + "/" + database;
        connection = null;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try {

            Class.forName(Driver);
            connection = DriverManager.getConnection(connectionString, username, password);
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static  ConnectionDB getInstance(){
        return instance;
    }

    public Connection getConnection() {

        return connection;
    }
}
