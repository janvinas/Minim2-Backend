package edu.upc.dsa.util;

import edu.upc.dsa.DB.SQLNotInsert;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QueryHelper {

    public static String CreateQueryINSERT(Object entity){

        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(entity.getClass().getSimpleName()).append(" (");

        String[] fields = ObjectHelper.getFields(entity);

        sb.append("ID");

        for(String field: fields){
            try {
                if(!field.equals("ID") && !entity.getClass().getDeclaredField(field).isAnnotationPresent(SQLNotInsert.class)) sb.append(", ").append(field);
            } catch (NoSuchFieldException ignored) { }
        }

        sb.append(") VALUES (0");

        for (String field: fields) {

            try {
                if (!field.equals("ID") && !entity.getClass().getDeclaredField(field).isAnnotationPresent(SQLNotInsert.class))  sb.append(", ?");
            } catch (NoSuchFieldException ignored) {}
        }
        sb.append(")");

        return sb.toString();

    }

    public static <T> String CreateQuerySELECT(Class<T> entity){

        StringBuffer sb = new StringBuffer("SELECT * FROM ");
        sb.append(entity.getSimpleName()).append(" WHERE ID=?;");

        return sb.toString();

    }

    public static <T> String CreateSelectAll(Class<T> entity){
        StringBuffer sb = new StringBuffer("SELECT * FROM ");
        sb.append(entity.getSimpleName()).append(";");
        return sb.toString();
    }

    public static String CreateSelectFINDALL(Class theClass, HashMap<String,String> params){

        Set<Map.Entry<String, String>> set = params.entrySet();

        StringBuffer sb = new StringBuffer("SELECT * FROM "+theClass.getSimpleName()+" WHERE 1=1");
        for (String key: params.keySet()) {
            sb.append(" AND "+key+"=?");
        }

        return sb.toString();
    }

    public static String CreateUpdate(Object entity){
        StringBuffer sb = new StringBuffer("UPDATE ");
        sb.append(entity.getClass().getSimpleName()).append(" SET username=?,password=? WHERE ID=?;");
        return sb.toString();
    }

    public static String CreateDelete(Object entity){
        StringBuffer sb = new StringBuffer("DELETE FROM ");
        sb.append(entity.getClass().getSimpleName()).append(" WHERE ID=?;");
        return sb.toString();
    }

}
