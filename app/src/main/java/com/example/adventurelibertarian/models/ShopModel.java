package com.example.adventurelibertarian.models;


public class ShopModel {
    public ShopModel(int color, double price, int zeroes){
        this.color = color;
        this.price = price;
        this.zeroes = zeroes;
    }

    public int getZeroes() {
        return zeroes;
    }

    public double getPrice() {
        return price;
    }

    public int getColor() {
        return color;
    }

    public boolean isBought() {
        return bought;
    }

    public void setbought(boolean bought){
        this.bought = bought;
    }

    private int color;
    private double price;
    private int zeroes;
    private boolean bought = false;
}
