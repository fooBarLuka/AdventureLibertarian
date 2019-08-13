package com.example.adventurelibertarian;

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

}
