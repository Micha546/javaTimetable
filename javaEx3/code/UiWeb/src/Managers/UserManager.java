package Managers;


import InformationHolders.User;
import Utils.SessionUtils;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

public class UserManager {

    private final Set<User> users = new HashSet<>();

    public synchronized boolean tryAddingUser(String newUserName)
    {
        User newUser = new User(newUserName);

        if(users.contains(newUser))
            return false;
        else
        {
            users.add(newUser);
            return true;
        }
    }

    public synchronized void removeUser(String userNameToRemove)
    {
        User userToRemove = new User(userNameToRemove);
        users.remove(userToRemove);
    }

    public synchronized Set<User> getUsers()
    {
        return new HashSet<>(users);
    }

    public synchronized User getUserByUserName(String username)
    {
        return users.stream()
                .filter(user -> user.getName().equals(username))
                .findFirst()
                .orElse(null);
    }

    public synchronized User getCurrentUser(HttpSession session)
    {
        return getUserByUserName(SessionUtils.getUserNameFromSession(session));
    }
}
