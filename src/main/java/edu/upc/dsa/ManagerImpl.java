package edu.upc.dsa;

import edu.upc.dsa.exceptions.MailNotFoundException;
import edu.upc.dsa.exceptions.ObjectNotFoundException;
import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.exceptions.WrongPasswordException;
import edu.upc.dsa.models.Store;
import edu.upc.dsa.models.StoreObject;
import edu.upc.dsa.models.User;

import java.security.DomainLoadStoreParameter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import javax.management.modelmbean.InvalidTargetObjectTypeException;

public class ManagerImpl implements Manager {
    private static Manager instance;
    protected List<User> users;
    protected List<StoreObject> objects;
    protected Store store;
    final static Logger logger = Logger.getLogger(ManagerImpl.class);

    private ManagerImpl() {
        this.users = new LinkedList<>();
        this.objects = new LinkedList<>();
    }

    public static Manager getInstance() {
        if (instance==null) instance = new ManagerImpl();
        return instance;
    }

    //User related
    public User addUser(User user){
        logger.info("Adding User: "+user);
        this.users.add(user);
        logger.info("User added");
        return user;
    }

    public User addUser(String username, String password, String mail){
        User user = new User(username, password,mail);
        return this.addUser(user);
    }

    public User getUser(String username){
        for (User u:users){
            if(u.getUsername().equals(username)){
                logger.info("Returning User: "+u);
                return u;
            }
        }
        logger.warn("User not found");
        return null;
    }

    public User getUser1(String username) throws UserNotFoundException{
        User u = getUser(username);
        if(u==null) throw new UserNotFoundException();
        return u;
    }

    public User getMail(String mail){
        for(User u:users){
            if(u.getMail().equals(mail)){
                logger.info("Returning User: "+u);
                return u;
            }
        }
        logger.warn("Mail not found");
        return null;
    }

    public User getMail1(String mail) throws MailNotFoundException {
        User u = getMail(mail);
        if(u==null) throw new MailNotFoundException();
        return u;
    }

    //Store related

    public StoreObject addToStore(StoreObject object){
        logger.info("Adding Object: "+object);
        this.objects.add(object);
        logger.info("Object added");
        return object;
    }

    public StoreObject addToStore(String name, int price){
        StoreObject object = new StoreObject(name,price);
        return this.addToStore(object);
    }

    public StoreObject getObject(String name){
        for(StoreObject o : objects){
            if (o.getName().equals(name)){
                logger.info("Returning Object:" +o);
                return o;
            }
        }
        logger.warn("Object not found");
        return null;
    }

    public StoreObject getObject1(String name){
        StoreObject o = getObject(name);
        if(o==null) throw new ObjectNotFoundException();
        return o;
    }

    //Register

    public boolean register(String username, String password, String mail){
        User u = getUser(username);
        if(u==null){
            logger.info("Adding user with credentials: Username="+username+", Password="+password+", Mail:"+mail);
            addUser(username,password,mail);
            return true;
        }
        return false;
    }

    //Login through username and through email

    public int login1(String username, String password){
        User u = getUser(username);
        if(u==null){
            logger.warn("User not found");
            return 2;
        }
        else{
            if(u.getPassword().equals(password)){
                logger.info("Login successful");
                return 0;
            }
            else{
                logger.warn("Wrong password");
                return 1;
            }
        }
    }

    public int login11(String username, String password) throws UserNotFoundException{
        int i = login1(username, password);
        if(i==2) throw new UserNotFoundException();
        return i;
    }

    public int login12(String username, String password) throws WrongPasswordException{
        int i = login1(username,password);
        if(i==1) throw new WrongPasswordException();
        return i;
    }

    public int login2(String mail, String password){
        User u = getMail(mail);
        if(u==null){
            logger.warn("User not found");
            return 2;
        }
        else{
            if(u.getPassword().equals(password)){
                logger.info("Login successful");
                return 0;
            }
            else{
                logger.warn("Wrong password");
                return 1;
            }
        }
    }

    public int login21(String username, String password) throws MailNotFoundException{
        int i = login1(username, password);
        if(i==2) throw new MailNotFoundException();
        return i;
    }

    public int login22(String username, String password) throws WrongPasswordException{
        int i = login1(username,password);
        if(i==1) throw new WrongPasswordException();
        return i;
    }

    //List of Users

    public List<User> findAllUsers(){
        return this.users;
    }

    public void deleteUser(String username){
        users.removeIf(u -> u.getUsername().equals(username));
    }

    public User updateUser1(User user, String username){
        int i = 0;
        for(User u:users){
            if(u.getUsername().equals(user.getUsername())){
                logger.info("Updating user: "+u);
                u.setUsername(username);
                logger.info("User with new username: "+u);
                return u;
            }
            i++;
        }
        return users.get(i);
    }

    public User updateUser2(User user, String password){
        int i = 0;
        for(User u:users){
            if(u.getPassword().equals(user.getPassword())){
                logger.info("Updating user: "+u);
                u.setPassword(password);
                logger.info("User with new password: "+u);
                return u;
            }
            i++;
        }
        return users.get(i);
    }

    //Get list of objects of a User and the Store

    public HashMap<StoreObject,Integer> getUserObjects(String username){
        User u = getUser(username);
        return u.getMyObjects();
    }

    public List<StoreObject> findAllObjects(){
        return this.objects;
    }

    //Clear & Size

    public void clearUsers(){
        users.clear();
    }

    public void clearObjects(){
        objects.clear();
    }

    public void clear(){
        clearUsers();
        clearObjects();
    }

    public int sizeUsers(){
        return users.size();
    }

    public int sizeObjects(){
        return objects.size();
    }

}