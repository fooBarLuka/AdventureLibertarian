package com.example.adventurelibertarian.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventurelibertarian.R;
import com.example.adventurelibertarian.adapters.ColorAdapter;
import com.example.adventurelibertarian.database.MyDataBase;
import com.example.adventurelibertarian.models.ColorModel;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class ShopFragment extends Fragment implements TabLayout.OnTabSelectedListener{

    private static ShopFragment shopFragment;

    public static ShopFragment getInstance(){
        if(shopFragment == null){
            shopFragment = new ShopFragment();

        }
        return shopFragment;
    }

    private RecyclerView shopRecyclerView;
    private TabLayout tabLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.shop_fragment_layout,container, false);
        shopRecyclerView = resultView.findViewById(R.id.shop_recycler_view_id);
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(resultView.getContext()));
        tabLayout = resultView.findViewById(R.id.shop_tab_layout_id);
        tabLayout.setOnTabSelectedListener(this);
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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getText().toString().equals(getString(R.string.managersTabName))){
            setColorAdapterToRecyclerView();
        } else if(tab.getText().toString().equals(getString(R.string.colorsTabName))){
            setColorAdapterToRecyclerView();
        } else {
            setColorAdapterToRecyclerView();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}
