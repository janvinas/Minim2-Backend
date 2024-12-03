package edu.upc.dsa.orm;

import edu.upc.dsa.DB.SQLNotInsert;
import edu.upc.dsa.util.QueryHelper;
import edu.upc.dsa.util.ObjectHelper;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class SessionImpl implements Session {
    private final Connection conn;

    public SessionImpl(Connection conn) {
        this.conn = conn;
    }

    public void save(Object entity){

        String InsertQuery = QueryHelper.CreateQueryINSERT(entity);

        PreparedStatement pstm = null;

        try {
            pstm = conn.prepareStatement(InsertQuery);

            int i = 1;

            for (String field: ObjectHelper.getFields(entity)) {
                if(entity.getClass().getDeclaredField(field).isAnnotationPresent(SQLNotInsert.class)) continue;
                pstm.setObject(i++, ObjectHelper.getter(entity, field));
            }

            pstm.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

    public void close(){
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object get(Class theClass, Object ID){
        return null;
    }

    public Object get(Class theClass, int ID){

        String sql = QueryHelper.CreateQuerySELECT(theClass);

        Object o = null;
        try {
            o = theClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        ResultSet res = null;

        ResultSetMetaData rsmd = null;
        try {
            rsmd = res.getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        int numColumns = 0;
        try {
            numColumns = rsmd.getColumnCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int i=0;

        while (i<numColumns) {
            String key = null;
            try {
                key = rsmd.getColumnName(i);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String value = null;
            try {
                value = res.getObject(i).toString();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            ObjectHelper.setter(o, key, value);

        }
        return null;
    }

    public void update(Object object,String username, String password, int ID){
        String sql = QueryHelper.CreateUpdate(object);
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(sql);
            pstm.setObject(1,username);
            pstm.setObject(2,password);
            pstm.setInt(3,ID);
            pstm.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void delete(Object object){
        String sql = QueryHelper.CreateDelete(object);
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(sql);
            pstm.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<Object> findAll(Class<T> theClass){
        String sql = QueryHelper.CreateSelectAll(theClass);
        PreparedStatement pstm = null;
        List<Object> results = new ArrayList<>();
        try{
            pstm = conn.prepareStatement(sql);
            ResultSet res = pstm.executeQuery();
            int columns = res.getMetaData().getColumnCount();

            while (res.next()) {
                List<T> row = new ArrayList<>();
                for (int i = 1; i <= columns; i++) {
                    row.add((T) res.getObject(i)); // Fetch column value dynamically
                }
                results.add(row); // Add the row to the results list
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return results;
    }

    public List<Object> findAll(Class theClass, HashMap params){
        String sql = QueryHelper.CreateSelectFINDALL(theClass,params);
        PreparedStatement pstm = null;
        List<Object> results = new ArrayList<>();
        try{
            pstm = conn.prepareStatement(sql);
            ResultSet res = pstm.executeQuery();
            int columns = res.getMetaData().getColumnCount();

            while (res.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= columns; i++) {
                    row.add(res.getObject(i)); // Fetch column value dynamically
                }
                results.add(row); // Add the row to the results list
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return results;
    }

    public List<Object> query(String query,Class theClass, HashMap params){
        return null;
    }

}
