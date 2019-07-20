package com.example.adventurelibertarian;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ShopFragment extends Fragment {

    private static ShopFragment shopFragment;

    public static ShopFragment getInstance(){
        if(shopFragment == null){
            shopFragment = new ShopFragment();
        }
        return shopFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shop_fragment_layout,container, false);
    }
}
