package db;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static DBConnector instance;
    private Connection connection;
    private DBCredentials credentials;

    private DBConnector() {
        loadCredentials();
        if(credentials != null){
            initConnection();
        }
    }

    public static synchronized DBConnector getInstance() {
        if(instance == null) {
            instance = new DBConnector();
        }
        return instance;
    }

    private void initConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://"+
                            credentials.hostname+":"+
                            credentials.port+"/"+
                            credentials.schema,
                            credentials.user,
                            credentials.password);

            System.out.println("connection initialized");
        } catch (SQLException e) {
            System.out.println("Error connecting to DB. Reason - " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Sorry, driver not found. Real reason - " + e.getMessage());
        }
    }

    public Connection getConnection() {
        if(connection == null){
            initConnection();
        }
        return connection;
    }

    private void loadCredentials() {
        Gson gson = new Gson();
        try {
            DBCredentials credentials = gson.fromJson(new FileReader("db_settings.json"), DBCredentials.class);
            this.credentials = credentials;
        } catch (FileNotFoundException e) {
            System.out.println("Error reading credentials." + e.getMessage());
        }
    }

    private class DBCredentials{
        private String hostname;
        private int port;
        private String user;
        private String password;
        private String schema;
    }

    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Closing connection failed." + e.getMessage());
        }
        connection = null;
    }
}
