package com.example.adventurelibertarian.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.adventurelibertarian.models.ManagerModel;

import java.util.List;

@Dao
public interface ManagerDao {

    @Insert
    void createAll(List<ManagerModel> managerModels);

    @Query("SELECT * FROM ManagerModel")
    List<ManagerModel> getManagers();

    @Update
    void updateManagerModel(ManagerModel managerModel);
}
