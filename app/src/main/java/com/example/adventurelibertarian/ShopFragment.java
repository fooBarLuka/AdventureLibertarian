package com.example.adventurelibertarian;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventurelibertarian.adapters.ShopAdapter;
import com.example.adventurelibertarian.models.ShopModel;

import java.util.ArrayList;
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
        ShopAdapter shopAdapter = new ShopAdapter();
        shopAdapter.setItems(getBackgroundColorItems());
        shopRecyclerView.setAdapter(shopAdapter);
        return resultView;
    }

    private List<ShopModel> getBackgroundColorItems(){
        List<ShopModel> shopModels = new ArrayList<>();

        shopModels.add(new ShopModel(Color.parseColor("#ffffff"), 1000,0));
        shopModels.add(new ShopModel(Color.parseColor("#000000"), 100000,0));
        shopModels.add(new ShopModel(Color.RED, 1000,6));

        return shopModels;
    }
}
