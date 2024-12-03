package edu.upc.dsa.dao;


import edu.upc.dsa.models.User;
import edu.upc.dsa.orm.FactorySession;
import edu.upc.dsa.orm.Session;

import java.util.List;

public class UserDAOImpl implements UserDAO {

    public int addUser(String username, String password, String mail){
        Session session = null;
        try {
            session = FactorySession.openSession();
            User user = new User(username,password,mail);
            session.save(user);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            session.close();
        }
        return 0;
    }

    public User getUser(int UserID){
        Session session = null;
        User user = null;
        try{
            session = FactorySession.openSession();
            user =(User) session.get(User.class, UserID);
            return user;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        finally {
            session.close();
        }
    }

    public void updateUser(int UserID, String username, String password){
        Session session = null;
        User user = null;
        try{
            session = FactorySession.openSession();
            user = (User) session.get(User.class, UserID);
            session.update(user,username,password,UserID);
        }catch(RuntimeException e){
            throw new RuntimeException(e);
        }
        finally{
            session.close();
        }
    }

    public void deleteUser(int UserID) {
        Session session = null;
        User user = null;
        try{
            session = FactorySession.openSession();
            user = (User) session.get(User.class, UserID);
            session.delete(user);
        }
        catch(RuntimeException e){
            throw new RuntimeException(e);
        }
        finally{
            session.close();
        }
    }

    public List<User> getUsers() {
        Session session = null;
        List<User> users = null;
        try {
            session = FactorySession.openSession();
            //users = session.findAll(User.class);
            return null;
        }catch(RuntimeException e){
            throw new RuntimeException(e);
        }
        finally {
            session.close();
        }
    }
}
