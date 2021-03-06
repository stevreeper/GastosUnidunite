package services;

import dao.Users;
import logic.User;

public class UserService {
    private static Users users;

    public UserService() throws Exception{
        this.users = new Users();
    }

    public User login(String email, String password) throws Exception {
        User user = users.getObjectByEmail(email);
        if (user != null && email.equals(user.getEmail()))
            if (user.getPassword().equals(password))
                return user;
        return new User();
    }

    public void addUser(String email, String pass, boolean isAdmin) throws Exception {
        users.addObject(new User(email, pass, isAdmin));
    }

    public void updateUser(User user) throws Exception {
        users.updateObject(user);
    }
}
