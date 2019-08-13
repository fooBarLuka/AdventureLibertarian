package com.example.adventurelibertarian.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.adventurelibertarian.ColorDao;
import com.example.adventurelibertarian.models.ColorModel;

@Database(entities = {ColorModel.class}, exportSchema = false, version = 1)
public abstract class MyDataBase extends RoomDatabase {
    public abstract ColorDao getColorDao();

    public static MyDataBase getInstance(){
        return myDataBase;
    }

    public static void buildDatabase(Context context){
        if(myDataBase == null){
            myDataBase = Room.databaseBuilder(context, MyDataBase.class, MY_DATABASE_NAME)
                    .build();
        }
    }

    public static final String MY_DATABASE_NAME = "lukas db name";
    private static MyDataBase myDataBase;
}
