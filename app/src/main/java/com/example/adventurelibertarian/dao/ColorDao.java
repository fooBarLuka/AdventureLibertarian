package com.example.adventurelibertarian.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.adventurelibertarian.models.ColorModel;

import java.util.List;

@Dao
public interface ColorDao {

    @Query("SELECT * FROM ColorModel")
    List<ColorModel> getAllColors();

    @Insert
    void createAll(List<ColorModel> colorModels);

    @Update
    void updateColorModel(ColorModel colorModel);

    @Query("UPDATE ColorModel SET 'set' = :chosen WHERE id = :id")
    void setChosenColor(boolean chosen, long id);

}
