package com.example.adventurelibertarian.models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BackgroundColorModel {

    public BackgroundColorModel(int color, double price, int zeroes){
        this.color = color;
        this.price = price;
        this.zeroes = zeroes;
    }

    @PrimaryKey
    public Long id;

    public int color;

    public double price;

    public int zeroes;

    public boolean bought = false;

    public boolean set = false;

}
