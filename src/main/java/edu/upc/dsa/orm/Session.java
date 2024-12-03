package edu.upc.dsa.orm;

import java.util.HashMap;
import java.util.List;

public interface Session {

    void save(Object entity);
    void close();
    Object get(Class theClass, Object ID);
    void update(Object object,String username, String password, int ID);
    void delete(Object object);
    <T> List<Object> findAll(Class<T> theClass);
    List<Object> findAll(Class theClasse, HashMap params);
    List<Object> query(String query, Class theClass, HashMap params);

}
