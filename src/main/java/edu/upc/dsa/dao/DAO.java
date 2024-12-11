package edu.upc.dsa.dao;

import edu.upc.dsa.Manager;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.InventoryObject;
import edu.upc.dsa.models.StoreObject;
import edu.upc.dsa.models.User;
import edu.upc.dsa.models.UserToken;
import edu.upc.dsa.orm.FactorySession;
import edu.upc.dsa.orm.Session;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DAO implements Manager {
    private static final Manager instance = new DAO();
    final static Logger logger = Logger.getLogger(DAO.class);
    protected TreeMap<String, UserToken> tokens;

    private final Session session;

    private DAO(){
        session = FactorySession.openSession();
    }

    public static Manager getInstance(){
        return instance;
    }

    @Override
    public User addUser(User t) {
        try{
            return session.save(t);
        }catch(SQLException e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public User addUser(String username, String password, String email) {
        return addUser(new User(username, password, email));
    }

    @Override
    public User getUser(String username) throws UserNotFoundException {
        try {
            User u = session.findAll(User.class, Map.of("username", username)).get(0);
            if(u == null) throw new UserNotFoundException();
            return u;
        } catch (SQLException e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public User getMail(String mail) throws MailNotFoundException {
        try {
            User u = session.findAll(User.class, Map.of("mail", mail)).get(0);
            if(u == null) throw new MailNotFoundException();
            return u;
        } catch (SQLException e) {
            throw new MailNotFoundException();
        }
    }

    @Override
    public void addPuntos(String username, int puntos) throws UserNotFoundException {
        try{
            User u = session.findAll(User.class, Map.of("username", username)).get(0);
            if(u == null) throw new UserNotFoundException();
            session.update(User.class, Map.of("puntos", u.getPuntos() + puntos), Map.of("username", username));
        }catch(SQLException e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public StoreObject addToStore(StoreObject object) {
        try{
            return session.save(object);
        }catch(SQLException e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public StoreObject addToStore(String name, double price, String URL) {
        return addToStore(new StoreObject(name, price, URL));
    }

    @Override
    public StoreObject getObject(String name) throws ObjectNotFoundException {
        try{
            StoreObject o = session.findAll(StoreObject.class, Map.of("name", name)).get(0);
            if(o == null) throw new ObjectNotFoundException();
            return o;
        }catch(SQLException e) {
            throw new ObjectNotFoundException();
        }
    }

    @Override
    public void buyObject(String username, String objectName, int quantity) throws UserNotFoundException, ObjectNotFoundException, NotEnoughMoneyException {
        try{
            User u = session.findAll(User.class, Map.of("username", username)).get(0);
            if(u == null) throw new UserNotFoundException();
            StoreObject o = session.findAll(StoreObject.class, Map.of("name", objectName)).get(0);
            if(o == null) throw new ObjectNotFoundException();

        }catch(SQLException e){
            throw new UserNotFoundException();
        }
    }

    @Override
    public boolean register(String username, String password, String mail) {
        return false;
    }

    @Override
    public User login1(String username, String password) throws UserNotFoundException, WrongPasswordException {
        return null;
    }

    @Override
    public User login2(String mail, String password) throws UserNotFoundException, WrongPasswordException {
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        return List.of();
    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public User updateUser1(User t, String username) {
        return null;
    }

    @Override
    public User updateUser2(User t, String password) {
        return null;
    }

    @Override
    public ArrayList<InventoryObject> getUserObjects(String username) throws UserNotFoundException {
        return null;
    }

    @Override
    public List<StoreObject> findAllObjects() {
        return List.of();
    }

    @Override
    public UserToken generateToken(String username) {
        UserToken token = new UserToken();
        tokens.put(username,token);
        return token;
    }

    @Override
    public boolean validateToken(String username, String token) {
        if(token == null) return false;
        UserToken Usertoken = tokens.get(username);
        if(Usertoken == null) return false;
        if(!Usertoken.getToken().equals(token)) return false;
        return !Usertoken.hasExpired();
    }

    @Override
    public void deleteToken(String username) {
        tokens.remove(username);
    }

    @Override
    public int sizeUsers() {
        return 0;
    }

    @Override
    public int sizeObjects() {
        return 0;
    }
}
