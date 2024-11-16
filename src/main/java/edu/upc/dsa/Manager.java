package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.InventoryObject;
import edu.upc.dsa.models.StoreObject;
import edu.upc.dsa.models.User;
import edu.upc.dsa.models.UserToken;

import java.util.ArrayList;
import java.util.List;

public interface Manager {

    //User related
    User addUser(User t);
    User addUser(String username, String password, String email);
    User getUser(String username) throws UserNotFoundException;
    User getMail (String mail) throws MailNotFoundException;

    //Store related
    StoreObject addToStore(StoreObject object);
    StoreObject addToStore(String name, double price);
    StoreObject getObject(String name) throws ObjectNotFoundException;

    void buyObject(String username, String objectName, int quantity) throws UserNotFoundException, ObjectNotFoundException, NotEnoughMoneyException;

    //Register
    boolean register(String username, String password, String mail);

    //Login through username and through mail
    User login1(String username, String password) throws UserNotFoundException, WrongPasswordException;
    User login2(String mail, String password) throws UserNotFoundException, WrongPasswordException;

    //List of Users
    List<User> findAllUsers();
    void deleteUser(String username);
    User updateUser1(User t, String username);
    User updateUser2(User t, String password);

    //Get the Objects of a User and the Store
    ArrayList<InventoryObject> getUserObjects(String username) throws UserNotFoundException;
    List<StoreObject> findAllObjects();

    //Token related
    UserToken generateToken(String username);
    boolean validateToken(String username, String token);
    void deleteToken(String username);

    //Clear and Size of lists used (Users & StoreObjects)
    void clear();
    void clearUsers();
    void clearObjects();
    int sizeUsers();
    int sizeObjects();
}
