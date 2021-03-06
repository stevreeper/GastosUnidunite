package dao;

import exceptions.UserException;
import interfaces.Dao;
import logic.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Users implements Dao<User> {
    private String host = "35.198.17.15";
    private String database = "SqlUnidunite";
    private String user = "appgerencial";
    private String password = "unidunitegerencial";
    private Connection connection;

    public Users() throws Exception {
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
    public List<User> getAllObjects() throws Exception {
        List<User> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * from tbl_user;");
        while (results.next()) {
            User user = new User(results.getString(2), results.getString(3), results.getBoolean(4));
            user.setId(results.getInt(1));
            list.add(user);
        }
        return list;
    }

    @Override
    public User getObject(Object key) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from tbl_user WHERE id = ?;");
        preparedStatement.setInt(1, (int) key);
        return getUser(preparedStatement);
    }

    public User getObjectByEmail(String email) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from tbl_user WHERE email = ?;");
        preparedStatement.setString(1, email);
        return getUser(preparedStatement);
    }

    private User getUser(PreparedStatement preparedStatement) throws SQLException, UserException {
        ResultSet results = preparedStatement.executeQuery();
        if (results.next()) {
            User user = new User(results.getString(2), results.getString(3), results.getBoolean(4));
            user.setId(results.getInt(1));
            return user;
        }
        return null;
    }

    @Override
    public void addObject(User o) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tbl_user (email, password, isAdmin) VALUES (?, ?, ?);");
        preparedStatement.setString(1, o.getEmail());
        preparedStatement.setString(2, o.getPassword());
        preparedStatement.setBoolean(3, o.isAdmin());
        preparedStatement.executeUpdate();
    }

    @Override
    public void updateObject(User o) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE tbl_user SET email = ?, password = ?, isAdmin = ? WHERE id = ?;");
        preparedStatement.setString(1, o.getEmail());
        preparedStatement.setString(2, o.getPassword());
        preparedStatement.setBoolean(3, o.isAdmin());
        preparedStatement.setInt(4, o.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void deleteObject(User o) throws Exception {

    }
}
