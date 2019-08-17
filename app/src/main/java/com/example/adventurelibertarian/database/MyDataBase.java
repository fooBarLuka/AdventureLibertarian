package com.example.adventurelibertarian.database;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.adventurelibertarian.dao.ColorDao;
import com.example.adventurelibertarian.fragment.ShopFragment;
import com.example.adventurelibertarian.models.ColorModel;

import java.util.ArrayList;
import java.util.List;

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

    public static void updateColorModelAsynchronous(final ColorModel colorModel){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                getInstance().getColorDao().updateColorModel(colorModel);
            }
        });
    }

    public static void initializeColorModels(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<ColorModel> colorModels = new ArrayList<>();

                ColorModel defaultColorModel = new ColorModel(Color.parseColor("#cccccc"), 0,0);
                defaultColorModel.bought = true;
                defaultColorModel.set = true;
                colorModels.add(defaultColorModel);//FFFF3D00 orange red // FFFF1744 purple red

                colorModels.add(new ColorModel(Color.parseColor("#b0000000"), 100000,0));
                colorModels.add(new ColorModel(Color.parseColor("#EDBB0707"), 1000,6));
                colorModels.add(new ColorModel(Color.parseColor("#FF4CAF50"), 1000,9));
                colorModels.add(new ColorModel(Color.parseColor("#77FFEA00"), 100, 12));
                colorModels.add(new ColorModel(Color.parseColor("#FFFFEA00"), 100, 15));

                colorModels.add(new ColorModel(Color.parseColor("#FFFF3D00"), 100, 15));
                colorModels.add(new ColorModel(Color.parseColor("#FFFF1744"), 100, 15));




                MyDataBase.getInstance().getColorDao().createAll(colorModels);
            }
        });
    }

    private static final String MY_DATABASE_NAME = "lukas db name";
    private static MyDataBase myDataBase;
}
