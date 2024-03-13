package com.example.fragmentsapp.models;

import java.util.ArrayList;

public class User {

    private String email;
    private String pass;
    private String userName;
    private String phoneNumber;

    public String getEmail() {
        return email;
    }
    public String getPass() {
        return pass;
    }
    public String getUserName() {
        return userName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User(String email, String pass, String userName, String phoneNumber) {
        this.email = email;
        this.pass = pass;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }
    public User(){

    }


}
