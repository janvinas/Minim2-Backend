package edu.upc.dsa.dao;

import edu.upc.dsa.Manager;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;
import edu.upc.dsa.orm.FactorySession;
import edu.upc.dsa.orm.Session;
import org.apache.log4j.Logger;

import java.sql.SQLException;
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
    public User addUser(User t) throws SQLException {
        return session.save(t);
    }

    @Override
    public User addUser(String username, String password, String email) throws SQLException {
        return addUser(new User(username, password, email));
    }

    @Override
    public User getUser(String username) throws UserNotFoundException, SQLException {
        User u = session.findAll(User.class, Map.of("username", username)).get(0);
        if(u == null) throw new UserNotFoundException();
        return u;
    }

    @Override
    public User getUserByID(String userID) throws UserNotFoundException, SQLException {
        User u = session.findAll(User.class, Map.of("ID", userID)).get(0);
        if(u == null) throw new UserNotFoundException();
        return u;
    }

    @Override
    public User getMail(String mail) throws MailNotFoundException, SQLException {
        User u = session.findAll(User.class, Map.of("mail", mail)).get(0);
        if(u == null) throw new MailNotFoundException();
        return u;
    }

    @Override
    public void addPuntos(String userID, int puntos) throws UserNotFoundException, SQLException {
        User u = session.findAll(User.class, Map.of("ID", userID)).get(0);
        if(u == null) throw new UserNotFoundException();
        session.update(User.class, Map.of("puntos", u.getPuntos() + puntos), Map.of("ID", userID));
    }

    @Override
    public StoreObject addToStore(StoreObject object) throws SQLException{
        return session.save(object);
    }

    @Override
    public StoreObject addToStore(String name, double price, String URL) throws SQLException {
        return addToStore(new StoreObject(name, price, URL));
    }

    @Override
    public StoreObject getObject(String name) throws ObjectNotFoundException, SQLException {
        StoreObject o = session.findAll(StoreObject.class, Map.of("name", name)).get(0);
        if(o == null) throw new ObjectNotFoundException();
        return o;
    }

    @Override
    public void buyObject(String userID, String objectID, int quantity) throws UserNotFoundException, ObjectNotFoundException, NotEnoughMoneyException, SQLException {
        User u = session.findAll(User.class, Map.of("ID", userID)).get(0);
        if(u == null) throw new UserNotFoundException();
        StoreObject o = session.findAll(StoreObject.class, Map.of("ID", objectID)).get(0);
        if(o == null) throw new ObjectNotFoundException();
        if(u.getMoney() < o.getPrice()*quantity) throw new NotEnoughMoneyException("Not enough money");

        List<Inventory> inventory = session.findAll(Inventory.class, Map.of("UserID", u.getID(), "ObjectID", o.getID()));
        if(inventory.isEmpty()){
            session.save(new Inventory(userID, objectID, quantity));
        }else{
            session.update(Inventory.class, Map.of("quantity", inventory.get(0).getQuantity() + quantity), Map.of("UserID", u.getID(), "ObjectID", o.getID()));
        }
        session.update(User.class, Map.of("money", u.getMoney() - o.getPrice()*quantity), Map.of("ID", userID));
    }

    @Override
    public User register(String username, String password, String mail) throws SQLException{
        if(!session.findAll(User.class, Map.of("username", username)).isEmpty()) return null;  // user already exists
        return session.save(new User(username, password, mail));
    }

    @Override
    public User login1(String username, String password) throws UserNotFoundException, WrongPasswordException, SQLException {
        User u = getUser(username);
        if(u == null) throw new UserNotFoundException();
        if(u.getPassword().equals(password)) return u;
        throw new WrongPasswordException();
    }

    @Override
    public User login2(String mail, String password) throws UserNotFoundException, WrongPasswordException, SQLException {
        User u = getMail(mail);
        if(u == null) throw new UserNotFoundException();
        if(u.getPassword().equals(password)) return u;
        throw new WrongPasswordException();
    }

    @Override
    public User login3(String userID, String password) throws UserNotFoundException, WrongPasswordException, SQLException {
        User u = getUserByID(userID);
        if(u == null) throw new UserNotFoundException();
        if(u.getPassword().equals(password)) return u;
        throw new WrongPasswordException();
    }

    @Override
    public List<User> findAllUsers() {
        return List.of();
    }

    @Override
    public void deleteUser(String username) throws SQLException {
        session.delete(User.class, Map.of("username", username));
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
    public List<Inventory> getUserObjects(String userID) throws UserNotFoundException, SQLException {
        return session.findAll(Inventory.class, Map.of("userID", userID));
    }

    @Override
    public List<StoreObject> findAllObjects() throws SQLException{
        return session.findAll(StoreObject.class);
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
