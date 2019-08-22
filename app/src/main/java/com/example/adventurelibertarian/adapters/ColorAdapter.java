package com.example.adventurelibertarian.adapters;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventurelibertarian.Activities.MainActivity;
import com.example.adventurelibertarian.R;
import com.example.adventurelibertarian.database.MyDataBase;
import com.example.adventurelibertarian.models.ColorModel;
import com.example.adventurelibertarian.presenter.MainActivityPresenter;
import com.example.adventurelibertarian.utils.CurrencyUtil;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorHolder> {

    public ColorAdapter(List<ColorModel> colorModels){
        this.colorModels = colorModels;
    }

    private List<ColorModel> colorModels;

    private static ColorHolder colorHolder;


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


    public class ColorHolder extends RecyclerView.ViewHolder {

        private TextView priceTextView;
        private View colorView;
        private Button actionButton;
        private TextView colorSetTextView;
        private ColorModel colorModel;

        public ColorHolder(@NonNull View itemView) {
            super(itemView);
            priceTextView = itemView.findViewById(R.id.price_text_view_id);
            colorView = itemView.findViewById(R.id.color_view_id);
            actionButton = itemView.findViewById(R.id.buy_button_id);
            colorSetTextView = itemView.findViewById(R.id.set_text_view_id);
        }

        public void setData(ColorModel colorModel){
            this.colorModel = colorModel;
            priceTextView.setText(String.format("%.2f",colorModel.price) + CurrencyUtil.reverseCurrencies.get(colorModel.zeroes));
            colorView.setBackgroundColor(colorModel.color);
            if(colorModel.set){
                actionButton.setVisibility(View.INVISIBLE);
                colorHolder = this;
            } else {
                if(colorModel.bought){
                    actionButton.setText("SET COLOR");
                }
            }

            setUIActions();
        }

        private void removeSetColor(){
            colorModel.set = false;
            updateUIAfterRemovingSetColor();
        }

        private void updateUIAfterBuying(){
            actionButton.setText("SET COLOR");
        }

        private void updateUIAfterSettingColor(){
            actionButton.setVisibility(View.INVISIBLE);
            colorSetTextView.setVisibility(View.VISIBLE);
        }

        private void updateUIAfterRemovingSetColor(){
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setText("SET COLOR");
            colorSetTextView.setVisibility(View.INVISIBLE);
        }

        private void setUIActions(){
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(colorModel.bought) { // if already bought, user wants to set color
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                MyDataBase.getInstance().getColorDao().setChosenColor(false, colorHolder.colorModel.id);
                                colorModel.set = true;
                                MyDataBase.updateColorModelAsynchronous(colorModel);
                            }
                        });

                        updateUIAfterSettingColor();
                        colorHolder.removeSetColor();
                        colorHolder = ColorHolder.this;

                        FactoriesAdapter.backgroundColor = colorModel.color;
                        MainActivity.mainActivity.redrawFactories();
                    } else {
                        if(MainActivityPresenter.getInstance().hasEnoughMoney(colorModel.price, colorModel.zeroes)){
                            colorModel.bought = true;
                            MyDataBase.updateColorModelAsynchronous(colorModel);
                            MainActivityPresenter.getInstance().subtractPrice(colorModel.price, colorModel.zeroes);
                            updateUIAfterBuying();
                        }
                    }
                }
            });
        }
    }
}
