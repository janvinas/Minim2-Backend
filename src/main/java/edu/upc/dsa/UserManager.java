package edu.upc.dsa;

import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.models.User;

import java.util.List;

public interface UserManager {


    public User addUser(String username, String password, String email);
    public User addUser(User t);
    public User getUser(String username);
    public User getUser2(String username) throws UserNotFoundException;

    public boolean login(String username, String password);
    public boolean login2(String mail, String password);

    public boolean register(String username, String password, String mail);

    //per canviar
    public List<User> findAll();
    public void deleteTrack(String id);
    public User updateTrack(User t);

    public void clear();
    public int size();
}
