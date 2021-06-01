package com.example.juiceup;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    private static ConnectionDB instance = new ConnectionDB();


    String ip;
    String port;
    String Driver;
    String database;
    String username;
    String password;
    String connectionString;
    Connection connection;


    private ConnectionDB(){


        ip = "192.168.56.1";
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
