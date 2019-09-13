package com.example.adventurelibertarian.database;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.adventurelibertarian.R;
import com.example.adventurelibertarian.dao.BackgroundColorDao;
import com.example.adventurelibertarian.dao.ColorDao;
import com.example.adventurelibertarian.dao.ManagerDao;
import com.example.adventurelibertarian.models.BackgroundColorModel;
import com.example.adventurelibertarian.models.ColorModel;
import com.example.adventurelibertarian.models.ManagerModel;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {ColorModel.class, ManagerModel.class, BackgroundColorModel.class}, exportSchema = false, version = 1)
public abstract class MyDataBase extends RoomDatabase {
    public abstract ColorDao getColorDao();

    public abstract ManagerDao getManagerDao();

    public abstract BackgroundColorDao getBackgroundColorDao();

    public static MyDataBase getInstance() {
        return myDataBase;
    }

    public static void buildDatabase(Context context) {
        if (myDataBase == null) {
            myDataBase = Room.databaseBuilder(context, MyDataBase.class, MY_DATABASE_NAME)
                    .build();
        }
    }

    public static void updateColorModelAsynchronous(final ColorModel colorModel) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                getInstance().getColorDao().updateColorModel(colorModel);
            }
        });
    }

    public static void updateBackgroundColorModelAsynchronous(final BackgroundColorModel backgroundColorModel){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                getInstance().getBackgroundColorDao().updateBackgroundColorModel(backgroundColorModel);
            }
        });
    }

    public static void initializeColorModels(final Context context) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<ColorModel> colorModels = new ArrayList<>();
                String drawableName = context.getResources().getResourceEntryName(R.drawable.white_default);

                ColorModel defaultColorModel = new ColorModel(drawableName, 0, 0);
                defaultColorModel.bought = true;
                defaultColorModel.set = true;
                colorModels.add(defaultColorModel);

                drawableName = context.getResources().getResourceEntryName(R.drawable.rgb1);
                colorModels.add(new ColorModel(drawableName, 100000, 0));

                drawableName = context.getResources().getResourceEntryName(R.drawable.rgb2);
                colorModels.add(new ColorModel(drawableName, 1000, 6));

                drawableName = context.getResources().getResourceEntryName(R.drawable.rgb3);
                colorModels.add(new ColorModel(drawableName, 1000, 9));

                drawableName = context.getResources().getResourceEntryName(R.drawable.rgb4);
                colorModels.add(new ColorModel(drawableName, 100, 12));
//                colorModels.add(new ColorModel(Color.parseColor("#FFFFEA00"), 100, 15));
//                colorModels.add(new ColorModel(Color.parseColor("#FFFF3D00"), 100, 15));
//                colorModels.add(new ColorModel(Color.parseColor("#FFFF1744"), 100, 15));

                MyDataBase.getInstance().getColorDao().createAll(colorModels);
            }
        });
    }

    public static void initializeBackgroundColorModels(final Context context){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final List<BackgroundColorModel> backgroundColorModels = new ArrayList<>();

                String entryName = context.getResources().getResourceEntryName(R.drawable.white_default);
                BackgroundColorModel defaultBackgroundColorModel = new BackgroundColorModel(entryName, 0, 0);
                defaultBackgroundColorModel.bought = true;
                defaultBackgroundColorModel.set = true;
                backgroundColorModels.add(defaultBackgroundColorModel);

                entryName = context.getResources().getResourceEntryName(R.drawable.rgb1);
                backgroundColorModels.add(new BackgroundColorModel(entryName, 100000, 0));

                entryName = context.getResources().getResourceEntryName(R.drawable.rgb2);
                backgroundColorModels.add(new BackgroundColorModel(entryName, 1000, 6));

                entryName = context.getResources().getResourceEntryName(R.drawable.rgb3);
                backgroundColorModels.add(new BackgroundColorModel(entryName, 1000, 9));

                entryName = context.getResources().getResourceEntryName(R.drawable.rgb4);
                backgroundColorModels.add(new BackgroundColorModel(entryName, 100, 12));
//                backgroundColorModels.add(new BackgroundColorModel(Color.parseColor("#FFFFEA00"), 100, 15));
//                backgroundColorModels.add(new BackgroundColorModel(Color.parseColor("#FFFF3D00"), 100, 15));
//                backgroundColorModels.add(new BackgroundColorModel(Color.parseColor("#FFFF1744"), 100, 15));

                MyDataBase.getInstance().getBackgroundColorDao().createAll(backgroundColorModels);
            }
        });
    }

    public static void initializeManagerModels() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<ManagerModel> managerModels = new ArrayList<>(); // 100, 2000, 10000, 10000/3, 10000/6 prices;
                managerModels.add(new ManagerModel(0, "ყანა",100, 0));
                managerModels.add(new ManagerModel(1, "ხინკლის კუჭები", 2000, 0));
                managerModels.add(new ManagerModel(2, "ბათე ჯენერალსი", 10000, 0));
                managerModels.add(new ManagerModel(3, "ჩიფსები", 10000, 3));
                managerModels.add(new ManagerModel(4, "პარლამენტი", 10000, 9));

                myDataBase.getManagerDao().createAll(managerModels);
            }
        });
    }

    public static void updateManagerModelAsynchronous(final ManagerModel managerModel){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                getInstance().getManagerDao().updateManagerModel(managerModel);
            }
        });
    }

    private static final String MY_DATABASE_NAME = "lukas db name";
    private static MyDataBase myDataBase;
}
