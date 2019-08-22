package com.example.adventurelibertarian.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ManagerModel {
    public ManagerModel(int factoryId, String factoryName, double price, int zeroes){
        this.factoryId = factoryId;
        this.factoryName = factoryName;
        this.price = price;
        this.zeroes = zeroes;
    }

    @PrimaryKey(autoGenerate = true)
    public long id;

    public int factoryId;

    public String factoryName;

    public double price;

    public int zeroes;

    public boolean bought = false;
}
