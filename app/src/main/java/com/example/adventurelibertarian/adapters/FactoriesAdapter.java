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

import com.example.adventurelibertarian.Activities.MainActivity;
import com.example.adventurelibertarian.R;
import com.example.adventurelibertarian.models.Factory;
import com.example.adventurelibertarian.utils.CurrencyUtil;
import com.example.adventurelibertarian.utils.SharedPreferencesConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FactoriesAdapter extends RecyclerView.Adapter<FactoriesAdapter.FactoryHolder> {

    private List<Factory> factories = new ArrayList<>();

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

    public void setFactories(List<Factory> factories, MainActivity mainActivity) {
        this.factories.clear();
        this.factories = factories;
    }

    public class FactoryHolder extends RecyclerView.ViewHolder implements SharedPreferencesConstants {
        private CircleImageView circleImageView;
        private TextView levelTextView;
        private TextView incomeTextView;
        private Button upgradeButton;
        private Button managerButton;
        private ProgressBar factoryProgressBar;
        private ProgressBar factoryEvolutionProgressBar;
        private TextView timeLeftTextView;
        private ImageView lockImageView;
        private View blurImageView;

        public FactoryHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.factory_image_id);
            levelTextView = itemView.findViewById(R.id.level_textView_id);
            incomeTextView = itemView.findViewById(R.id.income_text_view_id);
            upgradeButton = itemView.findViewById(R.id.upgrade_button_id);
            managerButton = itemView.findViewById(R.id.manager_button_id);
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
            managerButton.setText("manager: " + (long) factory.getManagerPrice() + CurrencyUtil.reverseCurrencies.get(factory.getManagerZeroes()));
            if (factory.getHasManager() || !factory.isOpen()) {
                managerButton.setVisibility(View.INVISIBLE);
            }
            if(factory.isOpen()){
                lockImageView.setVisibility(View.INVISIBLE);
                blurImageView.setVisibility(View.INVISIBLE);
            }
            updateIncomeTextView(factory);
            updateEvolutionProgressPar(factory);

            factory.setFactoryProgressBar(factoryProgressBar);
            factory.setTimeLeftTextView(timeLeftTextView);
            setActions(factory);
        }

        private void setActions(Factory factory) {
            initManagerAction(factory);
            initUpgradeAction(factory);
            initWorkingAction(factory);
        }

        private void initManagerAction(final Factory factory) {
            managerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(factory.hireManager()){
                        managerButton.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        private void initUpgradeAction(final Factory factory) {
            upgradeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(factory.upgradeFactory()){
                        updateUpgradeTextView(factory);
                        updateLevelTextView(factory);
                        updateIncomeTextView(factory);
                        updateEvolutionProgressPar(factory);

                        if(factory.getLevel() == 1){
                            if(!factory.getHasManager()) {
                                managerButton.setVisibility(View.VISIBLE);
                            }
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
                            factory.startWorking();
                        }
                    }
                }
            });
        }

        private void updateUpgradeTextView(Factory factory) {
            String textToDisplay = "buy: " + (long) factory.getUpgradePrice() + " " + CurrencyUtil.reverseCurrencies.get(factory.getUpgradeZeros());
            upgradeButton.setText(textToDisplay);
        }

        private void updateLevelTextView(Factory factory) {
            levelTextView.setText("" + factory.getLevel());
        }

        private void updateIncomeTextView(Factory factory) {
            incomeTextView.setText((long) factory.getIncome() + " " + CurrencyUtil.reverseCurrencies.get(factory.getZeroes()));
        }

        private void updateEvolutionProgressPar(Factory factory) {
            factoryEvolutionProgressBar.setProgress((factory.getLevel() % 25) * 4);
        }
    }
}
