package com.example.adventurelibertarian.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ColorModel {
    public ColorModel(String entryName, double price, int zeroes){
        this.entryName = entryName;
        this.price = price;
        this.zeroes = zeroes;
    }

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String entryName;
    public double price;
    public int zeroes;
    public boolean bought = false;
    public boolean set = false;
}
