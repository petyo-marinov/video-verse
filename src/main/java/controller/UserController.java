package controller;

import dao.UserDBDAO;
import dao.VideoDBDAO;
import model.User;
import model.Video;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserController {

    private static UserController instance;

    private UserController(){

    }

    public static UserController getInstance() {
        if(instance == null){
            instance = new UserController();
        }
        return instance;
    }

    public static User register(String email, String username, String password, int age, String gender) throws Exception{
        //TODO hash password
        //TODO validate input
        validateUsername(username);
        validatePassword(password);
        validateEmail(email);
        validateAge(age);
        validateGender(gender);
        password = hashPassword(password);

        return UserDBDAO.getInstance().insertUser(email,username,password,age,gender);
    }

    public List<Video> getLikedVideos(int userId) throws Exception {
        if(UserDBDAO.getInstance().getById(userId) == null){
            throw new Exception("user does not exist");
        }
        return VideoDBDAO.getInstance().getLikedByUser(userId);
    }

    private static String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);

        return hashedPassword;
    }

    private static void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password should have a minimum length of 8 characters.");
        }

        // Contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password should contain at least one uppercase letter.");
        }

        // Contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password should contain at least one lowercase letter.");
        }

        // Contains at least one digit
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password should contain at least one digit.");
        }

        // Contains at least one special character
        if (!password.matches(".*[!@#$%-^&*()].*")) {
            throw new IllegalArgumentException("Password should contain at least one special character.");
        }
    }

    private static void validateGender(String gender) throws Exception {
        if(!gender.equals("M")  && !gender.equals("F")) {
            throw new Exception("Invalid gender");
        }
    }

    private static void validateAge(int age) throws Exception {
        if(age < 18) {
            throw new Exception("You must be at least 18 years old");
        }
    }

    private static void validateEmail(String email) throws Exception {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if(!email.matches(regex)){
            throw new Exception("Email invalid");
        }
        if(UserDBDAO.getInstance().existsEmail(email)){
            throw new Exception("Email already exists");
        }
    }

    private static void validateUsername(String username) throws Exception {
        if(username.length() < 2) {
            throw new Exception("Username too short");
        }
    }

}
