package com.example.adventurelibertarian.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventurelibertarian.R;
import com.example.adventurelibertarian.database.MyDataBase;
import com.example.adventurelibertarian.models.ManagerModel;
import com.example.adventurelibertarian.presenter.MainActivityPresenter;
import com.example.adventurelibertarian.utils.CurrencyUtil;

import java.util.List;

public class ManagerAdapter extends RecyclerView.Adapter<ManagerAdapter.ManagerHolder> {


    public ManagerAdapter(List<ManagerModel> managerModels){
        this.managerModels = managerModels;
    }

    private List<ManagerModel> managerModels;

    @NonNull
    @Override
    public ManagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ManagerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_recyclerview_item_layout, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ManagerHolder holder, int position) {
        holder.setData(managerModels.get(position));
    }

    @Override
    public int getItemCount() {
        return managerModels.size();
    }

    class ManagerHolder extends RecyclerView.ViewHolder{

        private TextView moneyTextView;
        private TextView factoryNameTextView;
        private Button buyButton;
        private TextView boughtTextView;

        private ManagerModel managerModel;

        public ManagerHolder(@NonNull View itemView) {
            super(itemView);

            moneyTextView = itemView.findViewById(R.id.manager_money_textview_id);
            factoryNameTextView = itemView.findViewById(R.id.manager_factory_name_textView_id);
            buyButton = itemView.findViewById(R.id.manager_button_id);
            boughtTextView = itemView.findViewById(R.id.manager_bought_textview_id);
        }

        public void setData(ManagerModel managerModel){
            moneyTextView.setText(managerModel.price + CurrencyUtil.reverseCurrencies.get(managerModel.zeroes));
            factoryNameTextView.setText(managerModel.factoryName);

            if(managerModel.bought){
                buyButton.setVisibility(View.GONE);
            } else {
                boughtTextView.setVisibility(View.GONE);
            }
            this.managerModel = managerModel;

            setUIActions();
        }

        private void setUIActions(){
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivityPresenter.getInstance().hasEnoughMoney(managerModel.price, managerModel.zeroes)){
                        managerModel.bought = true;
                        MyDataBase.updateManagerModelAsynchronous(managerModel);

                        buyButton.setVisibility(View.GONE);
                        boughtTextView.setVisibility(View.VISIBLE);

                        MainActivityPresenter.getInstance().getFactories().get(managerModel.factoryId).hireManager();
                    }
                }
            });
        }
    }
}
