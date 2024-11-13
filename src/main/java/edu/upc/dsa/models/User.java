package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;

import java.util.HashMap;
import java.util.List;

public class User {

    public String username;
    public String password;
    public String mail;
    public String cookies;
    public HashMap<StoreObject,Integer> myObjects;

    public User(String username, String password, String mail) {
        this.setUsername(username);
        this.setPassword(password);
        this.setMail(mail);
        this.cookies=null;
        this.myObjects = new HashMap<>();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCookies(){
        return cookies;
    }

    public void setCookies(String cookies){
        this.cookies = cookies;
    }

    public HashMap<StoreObject,Integer> getMyObjects(){
        return this.myObjects;
    }

    public void setMyObjects(HashMap<StoreObject,Integer> objects){
        this.myObjects = objects;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password +", mail=" + mail + "]";
    }

}