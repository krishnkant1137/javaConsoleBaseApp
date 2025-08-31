package service;

import java.sql.SQLException;

import dao.UserDAO;
import model.User;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public User loginOrRegister(String name, String contact) throws SQLException {
        return userDAO.findOrCreateUser(name, contact);
    }
}
