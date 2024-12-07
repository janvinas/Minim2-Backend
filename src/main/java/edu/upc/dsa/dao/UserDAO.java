package edu.upc.dsa.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.upc.dsa.models.InventoryObject;
import edu.upc.dsa.models.User;

import java.util.ArrayList;
import java.util.List;

public interface UserDAO {

    public User addUser(String username, String password, String mail);
    public User getUser(int UserID);
    public void updateUser(int UserID, String username, String password);
    public void deleteUser(int UserID);
    public List<User> getUsers();

}