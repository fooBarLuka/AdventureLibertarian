package com.example.adventurelibertarian;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventurelibertarian.adapters.ColorAdapter;
import com.example.adventurelibertarian.database.MyDataBase;
import com.example.adventurelibertarian.models.ColorModel;

import java.util.List;

public class ShopFragment extends Fragment {

    private static ShopFragment shopFragment;

    public static ShopFragment getInstance(){
        if(shopFragment == null){
            shopFragment = new ShopFragment();
        }
        return shopFragment;
    }

    private RecyclerView shopRecyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.shop_fragment_layout,container, false);
        shopRecyclerView = resultView.findViewById(R.id.shop_recycler_view_id);
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        setColorAdapterToRecyclerView();
        return resultView;
    }

    private void setColorAdapterToRecyclerView(){
        final ColorAdapter colorAdapter = new ColorAdapter();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final List<ColorModel> colorModels = MyDataBase.getInstance().getColorDao().getAllColors();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        colorAdapter.setItems(colorModels);
                        shopRecyclerView.setAdapter(colorAdapter);
                    }
                });
            }
        });
    }

    public void redrawShopModels(){
        shopRecyclerView.setAdapter(null);
        shopRecyclerView.setLayoutManager(null);
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setColorAdapterToRecyclerView();
    }
}
