package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;

public class User {


    String username;
    String password;
    String mail;

    public User() {
        this.setId(RandomUtils.getId());
    }
    public User(String username, String password, String mail) {
        this.username = username;
        this.password = password;
        this.mail = mail;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password +", mail=" + mail + "]";
    }

}