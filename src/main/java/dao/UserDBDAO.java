package dao;

import db.DBConnector;
import model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class UserDBDAO implements UserDAO{

    private static UserDBDAO instance;
    private Connection connection;

    private UserDBDAO(){
        connection = DBConnector.getInstance().getConnection();
    }

    public static UserDBDAO getInstance(){
        if(instance == null){
            instance = new UserDBDAO();
        }
        return instance;
    }

    @Override
    public User insertUser(String email, String username, String password, int age, String gender) {
        String sql = "INSERT INTO users (email, username, password, age, gender, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, email);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setInt(4, age);
            ps.setString(5, gender);
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.execute();
            ResultSet resultSet = ps.getGeneratedKeys();
            resultSet.next();
            long id = resultSet.getLong(1);

            return new User((int)id, username, password, email, age, gender, LocalDateTime.now());
        } catch (SQLException e) {
            System.out.println("Error inserting user - " + e.getMessage());
            return null;
        }
    }

    @Override
    public User getById(int id) throws Exception {
        String sql = "SELECT id, username, password, email, age, gender, created_at FROM users WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()) {
                User user = new User(resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getInt("age"),
                        resultSet.getString("gender"),
                        resultSet.getTimestamp("created_at").toLocalDateTime());
                return user;
            }
            else {
                throw new Exception("User not found");
            }
        } catch (SQLException e) {
            System.out.println("Error getting the user - " + e.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<User> getAll() {
        return null;
    }

    @Override
    public User editUser(User u) {
        return null;
    }

    @Override
    public void deleteUser(User u) {

    }

    @Override
    public boolean existsEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, email);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            return count > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting user - " + e.getMessage());
            return false;
        }
    }

}
