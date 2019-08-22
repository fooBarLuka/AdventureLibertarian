package com.example.adventurelibertarian.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventurelibertarian.R;
import com.example.adventurelibertarian.adapters.BackgroundColorAdapter;
import com.example.adventurelibertarian.adapters.ColorAdapter;
import com.example.adventurelibertarian.adapters.ManagerAdapter;
import com.example.adventurelibertarian.database.MyDataBase;
import com.example.adventurelibertarian.models.BackgroundColorModel;
import com.example.adventurelibertarian.models.ColorModel;
import com.example.adventurelibertarian.models.ManagerModel;
import com.example.adventurelibertarian.presenter.ShopFragmentPresenter;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class ShopFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private static ShopFragment shopFragment;

    public static ShopFragment getInstance() {
        if (shopFragment == null) {
            shopFragment = new ShopFragment();

        }
        return shopFragment;
    }

    private RecyclerView shopRecyclerView;
    private TabLayout tabLayout;

    private ManagerAdapter managerAdapter;
    private ColorAdapter colorAdapter;
    private BackgroundColorAdapter backgroundColorAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.shop_fragment_layout, container, false);
        shopRecyclerView = resultView.findViewById(R.id.shop_recycler_view_id);
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(resultView.getContext()));
        tabLayout = resultView.findViewById(R.id.shop_tab_layout_id);
        tabLayout.setOnTabSelectedListener(this);

        new ShopFragmentPresenter(this);
        ShopFragmentPresenter.getInstance().fetchManagerModels();

        return resultView;
    }

    public void setColorItemsToRecyclerView(final List<ColorModel> colorModels) {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            colorAdapter = new ColorAdapter(colorModels);
            setColorAdapter();
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    colorAdapter = new ColorAdapter(colorModels);
                    setColorAdapter();
                }
            });
        }
    }

    public void setBackgroundColorItemsToRecyclerview(final List<BackgroundColorModel> backgroundColorModels){
        if(Looper.myLooper() == Looper.getMainLooper()){
            backgroundColorAdapter = new BackgroundColorAdapter(backgroundColorModels);
            setBackgroundColorAdapter();
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    backgroundColorAdapter = new BackgroundColorAdapter(backgroundColorModels);
                    setBackgroundColorAdapter();
                }
            });
        }
    }

    public void setManagerItemsToRecyclerView(final List<ManagerModel> managerModels) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            managerAdapter = new ManagerAdapter(managerModels);
            setManagerAdapter();
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    managerAdapter = new ManagerAdapter(managerModels);
                    setManagerAdapter();
                }
            });
        }
    }

    private void setColorAdapter() {
        shopRecyclerView.setAdapter(null);
        shopRecyclerView.setAdapter(colorAdapter);
    }

    private void setManagerAdapter() {
        shopRecyclerView.setAdapter(null);
        shopRecyclerView.setAdapter(managerAdapter);
    }

    private void setBackgroundColorAdapter(){
        shopRecyclerView.setAdapter(null);
        shopRecyclerView.setAdapter(backgroundColorAdapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getText().toString().equals(getString(R.string.managersTabName))) {
            if (managerAdapter == null) {
                ShopFragmentPresenter.getInstance().fetchManagerModels();
            } else {
                setManagerAdapter();
            }
        } else if (tab.getText().toString().equals(getString(R.string.colorsTabName))) {
            if (colorAdapter == null) {
                ShopFragmentPresenter.getInstance().fetchColorModels();
            } else {
                setColorAdapter();
            }
        } else {
            if(backgroundColorAdapter == null){
                ShopFragmentPresenter.getInstance().fetchBackgroundColorModels();
            } else {
                setBackgroundColorAdapter();
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}
