package com.example.adventurelibertarian.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventurelibertarian.Activities.MainActivity;
import com.example.adventurelibertarian.R;
import com.example.adventurelibertarian.models.ColorModel;
import com.example.adventurelibertarian.utils.CurrencyUtil;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorHolder> {

    List<ColorModel> colorModels = new ArrayList();

    @NonNull
    @Override
    public ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ColorHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_recyclerview_item_layout,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ColorHolder holder, int position) {
        holder.setData(colorModels.get(position));
    }

    @Override
    public int getItemCount() {
        return colorModels.size();
    }

    public void setItems(List<ColorModel> colorModels) {
        this.colorModels = colorModels;
    }

    public class ColorHolder extends RecyclerView.ViewHolder {

        private TextView priceTextView;
        private View colorView;
        private Button buyButton;
        private RadioGroup radioGroup;
        private ColorModel colorModel;

        public ColorHolder(@NonNull View itemView) {
            super(itemView);
            priceTextView = itemView.findViewById(R.id.price_text_view_id);
            colorView = itemView.findViewById(R.id.color_view_id);
            buyButton = itemView.findViewById(R.id.buy_button_id);
        }

        public void setData(ColorModel colorModel){
            this.colorModel = colorModel;
            priceTextView.setText(String.format("%.2f",colorModel.price) + CurrencyUtil.reverseCurrencies.get(colorModel.zeroes));
            colorView.setBackgroundColor(colorModel.color);

            setUIActions();
        }

        private void setUIActions(){
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FactoriesAdapter.backgroundColor = colorModel.color;
                    MainActivity.mainActivity.redrawFactories();
                }
            });
        }
    }
}
