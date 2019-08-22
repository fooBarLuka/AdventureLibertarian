package com.example.adventurelibertarian.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.adventurelibertarian.models.BackgroundColorModel;

import java.util.List;

@Dao
public interface BackgroundColorDao {

    @Insert
    void createAll(List<BackgroundColorModel> backgroundColorModels);

    @Query("SELECT * FROM BackgroundColorModel")
    List<BackgroundColorModel> getAllBackgroundColorModels();

    @Update
    void updateBackgroundColorModel(BackgroundColorModel backgroundColorModel);

    @Query("UPDATE BackgroundColorModel SET 'set' = :chosen WHERE id = :id")
    void updateChosenColor(boolean chosen, long id);
}
