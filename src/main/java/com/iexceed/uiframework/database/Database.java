package com.iexceed.uiframework.database;

import com.iexceed.uiframework.utilites.ProjectConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Database {
    public static Properties config;
    public static ProjectConfigurator projectConfigurator;
    public Connection conn = null;
    public Statement statement = null;
    private static Logger LOGGER = LogManager.getLogger(Database.class);

    static {
        try {
            config = ProjectConfigurator.initializeProjectConfigurationsFromFile("src/main/resources/project.properties");
        } catch (IOException e) {
            LOGGER.error("Error in creating properties loader in Database class");
        }
    }

    public Statement getStatement() throws ClassNotFoundException, SQLException {
        try {
            LOGGER.info("JDBC driver Name and Database URL");
            String driver = config.getProperty("DbDriver");
            String connection = config.getProperty("DbUrl");
            LOGGER.info("Database Credentials");
            String username = config.getProperty("DbUserName");
            String password = config.getProperty("DbPassword");
            LOGGER.info("Register JDBC driver");
            Class.forName(driver);
            LOGGER.info("Open a connection");
            conn = DriverManager.getConnection(connection, username, password);
            LOGGER.info("Execute a query");
            statement = conn.createStatement();
            return statement;
        } catch (Exception e) {
            LOGGER.error("connection refused ");
        }
        return statement;
    }

    /**
     *
     * @param query Send the query statement to insert the data
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void insertData(String query) throws ClassNotFoundException, SQLException {
        Statement stmt = getStatement();
        stmt.executeUpdate(query);
    }
    /**
     *
     * @param query Send the query statement to read the data
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ResultSet getData(String query) throws ClassNotFoundException, SQLException {
        ResultSet data = getStatement().executeQuery(query);
        return data;
    }
    /**
     * @param query Send the query statement to update the data
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void updateData(String query) throws ClassNotFoundException, SQLException {
        getStatement().executeUpdate(query);
    }

    /**
     * Close the database connection
     * @throws SQLException
     */
    public void closeDb() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}

