package com.example.adventurelibertarian.adapters;

import android.os.AsyncTask;
import android.util.Log;
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
import com.example.adventurelibertarian.models.BackgroundColorModel;
import com.example.adventurelibertarian.presenter.MainActivityPresenter;
import com.example.adventurelibertarian.utils.CurrencyUtil;

import java.util.ArrayList;
import java.util.List;

public class BackgroundColorAdapter extends RecyclerView.Adapter<BackgroundColorAdapter.BackgroundColorHolder> {

    private static BackgroundColorHolder backgroundColorHolder;

    private List<BackgroundColorModel> backgroundColorModels = new ArrayList<>();

    public BackgroundColorAdapter(List<BackgroundColorModel> backgroundColorModels) {
        this.backgroundColorModels = backgroundColorModels;
    }

    @NonNull
    @Override
    public BackgroundColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BackgroundColorHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_recyclerview_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BackgroundColorHolder holder, int position) {
        holder.setData(backgroundColorModels.get(position));
    }

    @Override
    public int getItemCount() {
        return backgroundColorModels.size();
    }

    class BackgroundColorHolder extends RecyclerView.ViewHolder {

        private TextView priceTextView;
        private View colorView;
        private Button actionButton;
        private TextView chosenTextView;
        private BackgroundColorModel backgroundColorModel;

        private int resourceId;

        public BackgroundColorHolder(@NonNull View itemView) {
            super(itemView);

            priceTextView = itemView.findViewById(R.id.price_text_view_id);
            colorView = itemView.findViewById(R.id.color_view_id);
            actionButton = itemView.findViewById(R.id.buy_button_id);
            chosenTextView = itemView.findViewById(R.id.set_text_view_id);
        }

        public void setData(BackgroundColorModel backgroundColorModel){
            this.backgroundColorModel = backgroundColorModel;
            priceTextView.setText(String.format("%.2f", backgroundColorModel.price + CurrencyUtil.reverseCurrencies.get(backgroundColorModel.zeroes)));
            resourceId = colorView.getResources().getIdentifier(backgroundColorModel.entryName,
                    "drawable", colorView.getContext().getPackageName());
            colorView.setBackgroundResource(resourceId);
            if(backgroundColorModel.set){
                backgroundColorHolder = this;
                actionButton.setVisibility(View.INVISIBLE);
            } else {
                if(backgroundColorModel.bought){
                    priceTextView.setVisibility(View.INVISIBLE);
                    actionButton.setText("SET COLOR");
                }
            }

            setUIActions();
        }

        private void removeSetColor(){
            backgroundColorModel.set = false;
            updateUIAfterRemovingSetColor();
        }

        private void updateUIAfterBuying(){
            actionButton.setText("SET COLOR");
        }

        private void updateUIAfterSettingColor(){
            actionButton.setVisibility(View.INVISIBLE);
            chosenTextView.setVisibility(View.VISIBLE);
        }

        private void updateUIAfterRemovingSetColor(){
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setText("SET COLOR");
            chosenTextView.setVisibility(View.INVISIBLE);
        }

        private void setUIActions(){
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(backgroundColorModel.bought){

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                MyDataBase.getInstance().getBackgroundColorDao().updateChosenColor(false, backgroundColorHolder.backgroundColorModel.id);
                                backgroundColorModel.set = true;
                                MyDataBase.getInstance().getBackgroundColorDao().updateBackgroundColorModel(backgroundColorModel);
                            }
                        });

                        updateUIAfterSettingColor();
                        backgroundColorHolder.removeSetColor();
                        backgroundColorHolder = BackgroundColorHolder.this;


                        MainActivity.mainActivity.changeRecyclerViewBackgroundColor(resourceId);
                    } else {
                        if (MainActivityPresenter.getInstance().hasEnoughMoney(backgroundColorModel.price, backgroundColorModel.zeroes)) {
                            backgroundColorModel.bought = true;
                            MyDataBase.updateBackgroundColorModelAsynchronous(backgroundColorModel);
                            MainActivityPresenter.getInstance().subtractPrice(backgroundColorModel.price, backgroundColorModel.zeroes);
                            updateUIAfterBuying();
                        }
                    }
                }
            });
        }
    }
}
