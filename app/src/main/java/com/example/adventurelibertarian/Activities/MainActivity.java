package com.example.adventurelibertarian.Activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import com.example.adventurelibertarian.R;
import com.example.adventurelibertarian.adapters.FactoriesAdapter;
import com.example.adventurelibertarian.database.MyDataBase;
import com.example.adventurelibertarian.fragment.ShopFragment;
import com.example.adventurelibertarian.models.ColorModel;
import com.example.adventurelibertarian.presenter.MainActivityPresenter;
import com.example.adventurelibertarian.utils.CurrencyUtil;
import com.example.adventurelibertarian.utils.MyCountDownTimer;
import com.example.adventurelibertarian.utils.NotificationsUtil;
import com.example.adventurelibertarian.utils.SharedPreferencesConstants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements SharedPreferencesConstants, RewardedVideoAdListener {

    private RewardedVideoAd rewardedVideoAd;

    private boolean fragmentOpened = false;

    MainActivityPresenter mainActivityPresenter = new MainActivityPresenter(this);

    public static MainActivity mainActivity;

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

        MyDataBase.buildDatabase(this);

        CurrencyUtil.initCurrencies();

        MainActivityPresenter.getInstance().initFactories();
        initUI();
        loadGame();
        initOnRewardAds();
        initUIActions();

        MyCountDownTimer.doWork();
    }

    private void initUI() {
        currentMoneyTextView = findViewById(R.id.current_money_id);

        resetButton = findViewById(R.id.reset_toggle_button_id);

        shopButton = findViewById(R.id.shop_button_id);

        fragmentManager = getSupportFragmentManager();
        NotificationsUtil.createNotificationChannel(this);

        initBonusImageView();

        initRecyclerView();
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
        factoriesAdapter = new FactoriesAdapter(MainActivityPresenter.getInstance().getFactories());
        factoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        factoriesRecyclerView.setAdapter(factoriesAdapter);
    }

    public void changeRecyclerViewBackgroundColor(int color){
        factoriesRecyclerView.setBackgroundColor(color);
    }

    private void loadGame() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        if (!preferences.getBoolean(NEW_PLAYER, true)) {
            mainActivityPresenter.loadFactories(preferences);
            mainActivityPresenter.loadMoney(preferences);
            mainActivityPresenter.loadOfflineMoneyAndProgresses(preferences);
            loadFactoriesColor();
        } else {
            MyDataBase.initializeColorModels();
            MyDataBase.initializeManagerModels();
            MyDataBase.initializeBackgroundColorModels();
        }
    }

    private void loadFactoriesColor(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final List<ColorModel> colorModels = MyDataBase.getInstance().getColorDao().getAllColors();
                for(int i = 0; i < colorModels.size(); i++){
                    if(colorModels.get(i).set){
                        final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FactoriesAdapter.backgroundColor = colorModels.get(finalI).color;
                                redrawFactories();
                            }
                        });
                        break;
                    }
                }
            }
        });
    }

    public void redrawFactories(){
        factoriesRecyclerView.setAdapter(null);
        factoriesRecyclerView.setAdapter(factoriesAdapter);
    }

    public void updateCurrentMoney(double ownedMoney, int zeroes) {
        currentMoneyTextView.setText("$ " + String.format("%.2f", ownedMoney)+ CurrencyUtil.reverseCurrencies.get(zeroes));
    }


    private void initUIActions() {
        bonusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long twelveHour = 1000 * 60 * 12;
                mainActivityPresenter.getBonusForTime(twelveHour);
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
        mainActivityPresenter.saveInSharedPreferences(getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE),resetButton.isChecked());
        mainActivityPresenter.sendNotificationAndSaveDoneTime(getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE));
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