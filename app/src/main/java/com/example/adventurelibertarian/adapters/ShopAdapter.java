package com.example.adventurelibertarian.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventurelibertarian.Activities.MainActivity;
import com.example.adventurelibertarian.R;
import com.example.adventurelibertarian.models.ShopModel;
import com.example.adventurelibertarian.utils.CurrencyUtil;

import java.util.ArrayList;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopHolder> {

    List<ShopModel> shopModels = new ArrayList();

    @NonNull
    @Override
    public ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShopHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_recyclerview_item_layout,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShopHolder holder, int position) {
        holder.setData(shopModels.get(position));
    }

    @Override
    public int getItemCount() {
        return shopModels.size();
    }

    public void setItems(List<ShopModel> shopModels) {
        this.shopModels = shopModels;
    }

    public class ShopHolder extends RecyclerView.ViewHolder {

        private TextView priceTextView;
        private View colorView;
        private Button buyButton;
        private ShopModel shopModel;

        public ShopHolder(@NonNull View itemView) {
            super(itemView);
            priceTextView = itemView.findViewById(R.id.price_text_view_id);
            colorView = itemView.findViewById(R.id.color_view_id);
            buyButton = itemView.findViewById(R.id.buy_button_id);
        }

        public void setData(ShopModel shopModel){
            this.shopModel = shopModel;
            priceTextView.setText(String.format("%.2f",shopModel.getPrice()) + CurrencyUtil.reverseCurrencies.get(shopModel.getZeroes()));
            colorView.setBackgroundColor(shopModel.getColor());
//            buyButton.setText("");

            setUIActions();
        }

        private void setUIActions(){
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FactoriesAdapter.backgroundColor = shopModel.getColor();
                    MainActivity.mainActivity.redrawFactories();
                }
            });
        }
    }
}
