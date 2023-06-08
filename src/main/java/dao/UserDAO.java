package dao;

import model.User;

import java.util.ArrayList;

public interface UserDAO {

    User insertUser(String email, String username, String password, int age, String gender);
    User getById(int id) throws Exception;
    ArrayList<User> getAll();
    User editUser(User u);
    void deleteUser(User u);

    boolean existsEmail(String email);

}
