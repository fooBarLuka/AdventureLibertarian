package com.example.adventurelibertarian.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.example.adventurelibertarian.models.ManagerModel;

import java.util.List;

@Dao
public interface ManagerDao {

    @Query("SELECT * FROM ManagerModel")
    List<ManagerModel> getManagers();

    @Query("UPDATE ManagerModel Set bought = :bought WHERE factoryId = :factoryId")
    void updateManager(boolean bought, int factoryId);
}
