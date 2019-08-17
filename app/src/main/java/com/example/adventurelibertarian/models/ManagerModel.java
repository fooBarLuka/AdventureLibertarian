package com.example.adventurelibertarian.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ManagerModel {
    public ManagerModel(int factoryId, double price, int zeroes){
        this.factoryId = factoryId;
        this.price = price;
        this.zeroes = zeroes;
    }

    @PrimaryKey(autoGenerate = true)
    public long id;

    public int factoryId;

    public double price;

    public int zeroes;

    public boolean bought = false;
}
