package dao;

import interfaces.Dao;
import logic.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Clients implements Dao<Client> {
    private String host = "35.198.17.15";
    private String database = "SqlUnidunite";
    private String user = "appgerencial";
    private String password = "unidunitegerencial";
    private Connection connection;

    public Clients() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = String.format("jdbc:mysql://%s/%s", host, database);

        //Set connection properties
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        properties.setProperty("socketFacotry", "com.google.cloud.sql.mysql.SocketFactory");
        properties.setProperty("useSSL", "false");

        connection = DriverManager.getConnection(url, properties);

        if (connection == null)
            throw new Exception("Failed to create connection to database.");
    }

    @Override
    public List<Client> getAllObjects() throws Exception {
        List<Client> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * from tbl_client;");
        while (results.next()) {
            Client client = new Client(results.getString(2), results.getString(5), results.getString(4), results.getString(3), results.getInt(1));
            list.add(client);
        }
        return list;
    }

    @Override
    public Client getObject(Object key) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from tbl_client WHERE id = ?;");
        preparedStatement.setInt(1, (int) key);
        ResultSet results = preparedStatement.executeQuery();
        if (results.next())
            return new Client(results.getString(2), results.getString(5), results.getString(4), results.getString(3), results.getInt(1));
        return null;
    }

    @Override
    public void addObject(Client o) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tbl_client (id, name, phone, email, address) VALUES (?, ?, ?, ?, ?);");
        preparedStatement.setInt(1, o.getId());
        preparedStatement.setString(2, o.getName());
        preparedStatement.setString(3, o.getPhone());
        preparedStatement.setString(4, o.getEmail());
        preparedStatement.setString(5, o.getAddress());
        preparedStatement.executeUpdate();
    }

    @Override
    public void updateObject(Client o) throws Exception {
        PreparedStatement preparedStatement = this.connection.prepareStatement("UPDATE tbl_client SET name = ?, phone = ?, email = ?, address = ? WHERE id = ?;");
        preparedStatement.setString(1, o.getName());
        preparedStatement.setString(2, o.getPhone());
        preparedStatement.setString(3, o.getEmail());
        preparedStatement.setString(4, o.getAddress());
        preparedStatement.setInt(5, o.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void deleteObject(Client o) throws Exception {
        PreparedStatement preparedStatement = this.connection.prepareStatement("DELETE FROM tbl_client WHERE id = ?;");
        preparedStatement.setInt(1, o.getId());
        preparedStatement.executeUpdate();
    }
}
