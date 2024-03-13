package com.example.fragmentsapp.models;

public class Product {

    private String name;
    private int amount;

    public Product(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public Product(){

    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int value){ amount = value;}
}
