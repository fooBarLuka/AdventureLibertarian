package com.example.adventurelibertarian.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.example.adventurelibertarian.database.MyDataBase;
import com.example.adventurelibertarian.fragment.ShopFragment;
import com.example.adventurelibertarian.models.BackgroundColorModel;
import com.example.adventurelibertarian.models.ColorModel;
import com.example.adventurelibertarian.models.ManagerModel;

import java.util.List;

public class ShopFragmentPresenter {

    public ShopFragmentPresenter(ShopFragment shopFragment){
        shopFragmentPresenter = this;
        this.shopFragment = shopFragment;
    }

    private ShopFragment shopFragment;

    private static ShopFragmentPresenter shopFragmentPresenter;

    public static ShopFragmentPresenter getInstance(){
        return shopFragmentPresenter;
    }

    public void fetchManagerModels(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<ManagerModel> managerModels = MyDataBase.getInstance().getManagerDao().getManagers();
                shopFragment.setManagerItemsToRecyclerView(managerModels);
            }
        });
    }

    public void fetchColorModels(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<ColorModel> colorModels = MyDataBase.getInstance().getColorDao().getAllColors();
                shopFragment.setColorItemsToRecyclerView(colorModels);
            }
        });
    }

    public void fetchBackgroundColorModels(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<BackgroundColorModel> backgroundColorModels = MyDataBase.getInstance().getBackgroundColorDao().getAllBackgroundColorModels();
                shopFragment.setBackgroundColorItemsToRecyclerview(backgroundColorModels);
            }
        });
    }
}
