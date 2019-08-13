package com.example.adventurelibertarian.Activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventurelibertarian.MainActivityPresenter;
import com.example.adventurelibertarian.R;
import com.example.adventurelibertarian.ShopFragment;
import com.example.adventurelibertarian.utils.AlarmManagerUtil;
import com.example.adventurelibertarian.utils.SharedPreferencesConstants;
import com.example.adventurelibertarian.adapters.FactoriesAdapter;
import com.example.adventurelibertarian.models.Factory;
import com.example.adventurelibertarian.utils.CurrencyUtil;
import com.example.adventurelibertarian.utils.NotificationsUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements SharedPreferencesConstants, RewardedVideoAdListener {

    private RewardedVideoAd rewardedVideoAd;

    private boolean fragmentOpened = false;

    MainActivityPresenter mainActivityPresenter = new MainActivityPresenter(this);

    public static MainActivity mainActivity;

    public List<Factory> factories = new ArrayList<>();
    private RecyclerView factoriesRecyclerView;
    private FactoriesAdapter factoriesAdapter;

    private ToggleButton resetButton;

    private CircleImageView bonusImageView;

    private TextView currentMoneyTextView;

    private Button shopButton;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;

        CurrencyUtil.initCurrencies();

        initFactories();
        initUI();
        initOnRewardAds();
        initUIActions();
    }

    private void initFactories() {
        factories.clear();
        Factory agricultureFactory = new Factory(0, 1, 0, 2.3,
                10, 0,1.4, 100, 0, 1000, R.drawable.toxi, mainActivityPresenter);

        agricultureFactory.setOpen(true);
        factories.add(agricultureFactory);

        Factory xinkaliFactory = new Factory(1, 20, 0, 1.4,
                50,0, 1.5, 2000, 0, 2000, R.drawable.xink, mainActivityPresenter);

        factories.add(xinkaliFactory);

        Factory bateGeneralsFactory = new Factory(2, 1000, 0, 1.6,
                1000, 0, 1.6, 10000, 0, 10000, R.drawable.dummy_bate, mainActivityPresenter);

        factories.add(bateGeneralsFactory);

        Factory chips = new Factory(3, 100000, 0, 1.7,
                200000, 0,1.65, 10000, 3, 100000, R.drawable.chips, mainActivityPresenter);

        factories.add(chips);

        Factory parliament = new Factory(4, 100000, 9, 2.2,
                100000, 6,2.1, 10000, 6,150000, R.drawable.parliament, mainActivityPresenter);

        factories.add(parliament);
    }

    public void redrawFactories(){
        factoriesRecyclerView.setAdapter(null);
        factoriesRecyclerView.setLayoutManager(null);
        factoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        factoriesRecyclerView.setAdapter(factoriesAdapter);
    }

    private void initUI() {
        currentMoneyTextView = findViewById(R.id.current_money_id);

        resetButton = findViewById(R.id.reset_toggle_button_id);

        shopButton = findViewById(R.id.shop_button_id);
        fragmentManager = getSupportFragmentManager();

        initBonusImageView();

        loadGame();
        initRecyclerView();

        NotificationsUtil.createNotificationChannel(this);
    }

    private void initBonusImageView() {
        bonusImageView = findViewById(R.id.bonus_circle_image_view_id);
        Picasso.get().load(R.drawable.bonus_chest).into(bonusImageView);
        bonusImageView.setVisibility(View.INVISIBLE);
    }

    private void initOnRewardAds() {
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);

//        loadRewardedVideoAd();
        loadTestAd();
    }

    private void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd("ca-app-pub-3725698160831098/4545811217", new AdRequest.Builder().build());
    }

    private void loadTestAd() {
        rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }

    private void initRecyclerView() {
        factoriesRecyclerView = findViewById(R.id.factories_recycler_view_id);
        factoriesAdapter = new FactoriesAdapter(factories);
        factoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        factoriesRecyclerView.setAdapter(factoriesAdapter);
    }

    private void loadGame() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        if (!preferences.getBoolean(NEW_PLAYER, true)) {
            mainActivityPresenter.loadFactories(factories, preferences);
            mainActivityPresenter.loadMoney(preferences);
            mainActivityPresenter.loadOfflineMoneyAndProgresses(getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE));
        }
    }


    public void updateCurrentMoney(double ownedMoney, int zeroes) {
        currentMoneyTextView.setText("$ " + String.format("%.2f", ownedMoney)+ CurrencyUtil.reverseCurrencies.get(zeroes));
    }


    private void initUIActions() {
        bonusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long twelveHour = 1000 * 60 * 12;
                mainActivityPresenter.getBonusForTime(twelveHour, factories);
            }
        });

        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fragmentOpened){
                    openShopFragment();
                    fragmentOpened = true;
                } else {
                    closeShopFragment();
                    fragmentOpened = false;
                }
            }
        });
    }

    private void openShopFragment(){
        ShopFragment shopFragment = ShopFragment.getInstance();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragments_container_id, shopFragment);
        fragmentTransaction.commit();
    }

    private void closeShopFragment(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(ShopFragment.getInstance());
        fragmentTransaction.commit();
    }

    public void showBeforeRewardDialog(double bonusMoney, int bonusMoneyZeroes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Get 12 hours boost")
                .setMessage("bonus: " + (long) bonusMoney + CurrencyUtil.reverseCurrencies.get(bonusMoneyZeroes))
                .setPositiveButton("Watch AD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (rewardedVideoAd.isLoaded()) {
                            rewardedVideoAd.show();
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
//        Toast.makeText(this, (long) offlineMoney + CurrencyUtil.reverseCurrencies.get(offlineMoneyZeroes), Toast.LENGTH_LONG).show();
    }

    public void showAfterRewardDialog(double bonusMoney, int bonusMoneyZeroes) {
        createNonActionableDialog("Well Done", "You Got " +
                (long) bonusMoney + CurrencyUtil.reverseCurrencies.get(bonusMoneyZeroes) + "money").show();
    }

    public void showAfterIdleDialog(double offlineMoney, int offlineMoneyBonus) {
        createNonActionableDialog("Hello again Libertarian!", "Your Managers Earned $ " +
                (long) offlineMoney + CurrencyUtil.reverseCurrencies.get(offlineMoneyBonus) + " While You Were Out").show();
    }

    private AlertDialog createNonActionableDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true);

        return builder.create();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainActivityPresenter.saveInSharedPreferences(factories, resetButton.isChecked());
        sendNotificationAndSaveDoneTime();
    }

    private void sendNotificationAndSaveDoneTime() {
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
        long longestTimeUntilFinished = 0;
        for (int i = 0; i < factories.size(); i++) {
            Factory factory = factories.get(i);
            long timeLeft = factory.getMillisUntilFinished();
            editor.putLong(i + "done", factory.getWaitingTime() - timeLeft);
            if (timeLeft > longestTimeUntilFinished) {
                longestTimeUntilFinished = timeLeft;
            }
        }
        editor.apply();
        if (longestTimeUntilFinished >= 50000) {
            AlarmManagerUtil.setAlarmInTime(this, longestTimeUntilFinished);
        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        bonusImageView.setVisibility(View.INVISIBLE);
        mainActivityPresenter.sumBonus();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Log.e("tagi", "loaded");
        bonusImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.e("tagi", "opened");
    }

    @Override
    public void onRewardedVideoStarted() {
        Log.e("tagi", "started");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.e("tagi", "closed");
        loadTestAd();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.e("tagi", "left application");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Log.e("tagi", "failed to Load " + i);
        loadTestAd();
    }

    @Override
    public void onRewardedVideoCompleted() {
        Log.e("tagi", "completed");
    }
}