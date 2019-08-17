package com.example.adventurelibertarian.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ManagerModel {
    public ManagerModel(int factoryId, double price, int zeroes, boolean bought){
        this.factoryId = factoryId;
        this.price = price;
        this.zeroes = zeroes;
        this.bought = bought;
    }

    @PrimaryKey
    public int factoryId;

    public double price;

    public int zeroes;

    public boolean bought;
}
