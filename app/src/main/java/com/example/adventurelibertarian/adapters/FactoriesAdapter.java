package com.example.adventurelibertarian.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventurelibertarian.R;
import com.example.adventurelibertarian.models.Factory;
import com.example.adventurelibertarian.utils.CurrencyUtil;
import com.example.adventurelibertarian.utils.SharedPreferencesConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FactoriesAdapter extends RecyclerView.Adapter<FactoriesAdapter.FactoryHolder> {

    public static int backgroundColor = -1;

    private List<Factory> factories = new ArrayList<>();

    public FactoriesAdapter(List<Factory> factories){
        this.factories.clear();
        this.factories = factories;
    }

    @NonNull
    @Override
    public FactoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FactoryHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_recycler_view_item_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FactoryHolder factoryHolder, int i) {
        factoryHolder.setData(factories.get(i));
    }

    @Override
    public int getItemCount() {
        return factories.size();
    }


    public class FactoryHolder extends RecyclerView.ViewHolder implements SharedPreferencesConstants {
        private CircleImageView circleImageView;
        private TextView levelTextView;
        private TextView incomeTextView;
        private Button upgradeButton;
        private ProgressBar factoryProgressBar;
        private ProgressBar factoryEvolutionProgressBar;
        private TextView timeLeftTextView;
        private ImageView lockImageView;
        private View blurImageView;

        private View rootView;

        public FactoryHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView;

            circleImageView = itemView.findViewById(R.id.factory_image_id);
            levelTextView = itemView.findViewById(R.id.level_textView_id);
            incomeTextView = itemView.findViewById(R.id.income_text_view_id);
            upgradeButton = itemView.findViewById(R.id.upgrade_button_id);
            factoryProgressBar = itemView.findViewById(R.id.progressbar_id);
            factoryEvolutionProgressBar = itemView.findViewById(R.id.factory_evolution_progress_bar_id);
            timeLeftTextView = itemView.findViewById(R.id.times_left_text_view_id);
            lockImageView = itemView.findViewById(R.id.lock_image_view_id);
            blurImageView = itemView.findViewById(R.id.blur_view_id);
        }

        public void setData(Factory factory) {
            Picasso.get().load(factory.getImageId()).into(circleImageView);
            updateLevelTextView(factory);
            updateUpgradeTextView(factory);
            if (factory.isOpen()) {
                lockImageView.setVisibility(View.INVISIBLE);
                blurImageView.setVisibility(View.INVISIBLE);
            }
            updateIncomeTextView(factory);
            updateEvolutionProgressPar(factory);

            factory.setFactoryProgressBar(factoryProgressBar);
            factory.setTimeLeftTextView(timeLeftTextView);

            if(backgroundColor != -1){
                rootView.setBackgroundColor(backgroundColor);
            }

            setActions(factory);
        }

        private void setActions(Factory factory) {
            initUpgradeAction(factory);
            initWorkingAction(factory);
        }

        private void initUpgradeAction(final Factory factory) {
            upgradeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (factory.upgradeFactory()) {
                        updateUpgradeTextView(factory);
                        updateLevelTextView(factory);
                        updateIncomeTextView(factory);
                        updateEvolutionProgressPar(factory);

                        if (factory.getLevel() == 1) {
                            lockImageView.setVisibility(View.INVISIBLE);
                            blurImageView.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }

        private void initWorkingAction(final Factory factory) {
            factoryProgressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (factory.isOpen()) {
                        if (!factory.isWorking()) {
                            factory.startWorking(factory.getWaitingTime());
                        }
                    }
                }
            });
        }

        private void updateUpgradeTextView(Factory factory) {
            String textToDisplay = "buy: " + String.format("%.2f", factory.getUpgradePrice()) + " " + CurrencyUtil.reverseCurrencies.get(factory.getUpgradeZeros());
            upgradeButton.setText(textToDisplay);
        }

        private void updateLevelTextView(Factory factory) {
            levelTextView.setText("" + factory.getLevel());
        }

        private void updateIncomeTextView(Factory factory) {
            String incomeFormated = String.format("%.2f", factory.getIncome());
            incomeTextView.setText(incomeFormated + " " + CurrencyUtil.reverseCurrencies.get(factory.getZeroes()));
        }

        private void updateEvolutionProgressPar(Factory factory) {
            factoryEvolutionProgressBar.setProgress((factory.getLevel() % 25) * 4);
        }
    }
}
