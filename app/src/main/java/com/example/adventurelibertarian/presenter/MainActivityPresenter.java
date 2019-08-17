package com.example.adventurelibertarian.presenter;

import android.content.SharedPreferences;
import android.os.SystemClock;

import com.example.adventurelibertarian.Activities.MainActivity;
import com.example.adventurelibertarian.R;
import com.example.adventurelibertarian.models.Factory;
import com.example.adventurelibertarian.utils.AlarmManagerUtil;
import com.example.adventurelibertarian.utils.SharedPreferencesConstants;

import java.util.ArrayList;
import java.util.List;

public class MainActivityPresenter implements SharedPreferencesConstants {
    private int zeroes = 0;
    private double currentMoney = 0;

    private double offlineMoney = 0;
    private int offlineMoneyZeroes = 0;

    private MainActivity mainActivity;
    private static MainActivityPresenter mainActivityPresenter;

    private List<Factory> factories = new ArrayList<>();

    public MainActivityPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        mainActivityPresenter = this;
    }

    public static MainActivityPresenter getInstance(){
        return mainActivityPresenter;
    }

    public void initFactories() {
        factories.clear();
        Factory agricultureFactory = new Factory(0, 1, 0, 4.0,
                10, 0,1.4, 100, 0, 300, R.drawable.toxi, mainActivityPresenter);

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

    public void loadFactories(SharedPreferences preferences) {
        for (int i = 0; i < factories.size(); i++) {
            String factoryCostKey = i + UPGRADE_COST;
            String factoryUpgradeCostZeroesKey = i + UPGRADE_ZEROES;
            String factoryIncomeKey = i + INCOME;
            String factoryIncomeZeroes = i + INCOME_ZEROES;
            String factoryIsOpen = i + IS_OPEN;
            String levelKey = i + LEVEL;
            String incomeMultiplierKey = i + INCOME_MULTIPLIER;
            String upgradeMultiplierKey = i + UPGRADE_MULTIPLIER;
            String hasManager = i + HAS_MANAGER;
            Factory factory = factories.get(i);
            double upgradePrice = preferences.getFloat(factoryCostKey, (float) factory.getUpgradePrice());
            factory.setUpgradePrice(upgradePrice);

            int upgradeZeroes = preferences.getInt(factoryUpgradeCostZeroesKey, factory.getUpgradeZeros());
            factory.setUpgradeZeros(upgradeZeroes);

            double incomePrice = preferences.getFloat(factoryIncomeKey, (float) factory.getIncome());
            factory.setIncome(incomePrice);

            int factoryZeroes = preferences.getInt(factoryIncomeZeroes, factory.getZeroes());
            factory.setZeroes(factoryZeroes);

            boolean isOpen = preferences.getBoolean(factoryIsOpen, factory.isOpen());
            factory.setOpen(isOpen);

            int level = preferences.getInt(levelKey, factory.getLevel());
            factory.setLevel(level);

            double incomeMultiplier = preferences.getFloat(incomeMultiplierKey, (float) factory.getIncomeMultiplier());
            factory.setIncomeMultiplier(incomeMultiplier);

            double upgradeMultiplier = preferences.getFloat(upgradeMultiplierKey, (float) factory.getUpgradePriceMultiplier());
            factory.setUpgradePriceMultiplier(upgradeMultiplier);

            boolean factoryHasManager = preferences.getBoolean(hasManager, false);
            factory.setHasManager(factoryHasManager);
        }
    }

    public List<Factory> getFactories(){
        return factories;
    }

    public void loadMoney(SharedPreferences preferences) {
        currentMoney = preferences.getFloat("currentMoney", (float) currentMoney);
        zeroes = preferences.getInt("currency", zeroes);
    }

    public void loadOfflineMoneyAndProgresses(SharedPreferences preferences) {
        long prevTime = preferences.getLong(CURRENT_TIME, 0);
        long currentTime = SystemClock.elapsedRealtime();
        if(currentTime < prevTime){
            prevTime = preferences.getLong(SUBSTITUTE_CURRENT_TIME,0);
            currentTime = System.currentTimeMillis();
        }
        for (int i = 0; i < factories.size(); i++) {
            long millisDone = preferences.getLong(i + "done", 0);
            factories.get(i).loadFactoryProgress(currentTime, prevTime, millisDone);
        }
        sumMoney(offlineMoney, offlineMoneyZeroes);
        mainActivity.showAfterIdleDialog(offlineMoney, offlineMoneyZeroes);
    }

    public void getBonusForTime(long time) {
        offlineMoney = 0;
        offlineMoneyZeroes = 0;
        for (int i = 0; i < factories.size(); i++) {
            Factory factory = factories.get(i);
            if (factory.getHasManager() && factory.isOpen()) {
                double incomeWithoutCurrency = factory.getIncome() / factory.getWaitingTime() * time;
                sumOfflineMoney(incomeWithoutCurrency, factory.getZeroes());
            }
        }
        mainActivity.showBeforeRewardDialog(offlineMoney, offlineMoneyZeroes);
    }

    public void sumBonus() {
        sumMoney(offlineMoney, offlineMoneyZeroes);
        mainActivity.showAfterRewardDialog(offlineMoney, offlineMoneyZeroes);
    }

    public void saveInSharedPreferences(SharedPreferences preferences, boolean delete) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(NEW_PLAYER, false);
        editor.putFloat("currentMoney", (float) currentMoney);
        editor.putInt("currency", zeroes);
        editor.putLong(CURRENT_TIME, SystemClock.elapsedRealtime());
        editor.putLong(SUBSTITUTE_CURRENT_TIME, System.currentTimeMillis());

        for (int i = 0; i < factories.size(); i++) {
            String cost = i + UPGRADE_COST;
            String upgradeZeros = i + UPGRADE_ZEROES;
            String income = i + INCOME;
            String incomeZeroes = i + INCOME_ZEROES;
            String isOpen = i + IS_OPEN;
            String level = i + LEVEL;
            String incomeMultiplier = i + INCOME_MULTIPLIER;
            String upgradeMultiplier = i + UPGRADE_MULTIPLIER;
            String hasManager = i + HAS_MANAGER;
            String progressDone = i + "done";
            Factory factory = factories.get(i);
            editor.putFloat(cost, (float) factory.getUpgradePrice());
            editor.putInt(upgradeZeros, factory.getUpgradeZeros());
            editor.putFloat(income, (float) factory.getIncome());
            editor.putInt(incomeZeroes, factory.getZeroes());
            editor.putBoolean(isOpen, factory.isOpen());
            editor.putInt(level, factory.getLevel());
            editor.putFloat(incomeMultiplier, (float) factory.getIncomeMultiplier());
            editor.putFloat(upgradeMultiplier, (float) factory.getUpgradePriceMultiplier());
            editor.putBoolean(hasManager, factory.getHasManager());
            editor.putLong(progressDone, factory.getWaitingTime() - factory.getMillisUntilFinished());
        }

        editor.apply();
        if (delete) {
            editor.clear().commit();
        }
    }

    private void checkForCurrencyChange() {
        if (currentMoney >= 1000000) {
            zeroes += 3;
            currentMoney /= 1000;
        } else if (currentMoney < 1000 && zeroes != 0) {
            zeroes -= 3;
            currentMoney *= 1000;
        }
        mainActivity.updateCurrentMoney(currentMoney, zeroes);
    }

    public void sumOfflineMoney(double moneyToAdd, int zeroes) {
        if (this.offlineMoneyZeroes >= zeroes) {
            offlineMoney += (moneyToAdd * Math.pow(10, zeroes - offlineMoneyZeroes));
        } else {
            offlineMoney *= Math.pow(10, offlineMoneyZeroes - zeroes);
            offlineMoney += moneyToAdd;
            this.offlineMoneyZeroes = zeroes;
        }
        checkOfflineCurrencyChange();
    }

    private void checkOfflineCurrencyChange() {
        if (offlineMoney >= 1000000) {
            offlineMoneyZeroes += 3;
            offlineMoney /= 1000;
        }
    }

    public void subtractPrice(double moneyToSubtract, int zeroes) {
        moneyToSubtract /= Math.pow(10, this.zeroes - zeroes);
        currentMoney -= moneyToSubtract;

        checkForCurrencyChange();
    }

    public void sumMoney(double moneyToAdd, int zeroes) {
        if (this.zeroes >= zeroes) {
            currentMoney += (moneyToAdd * Math.pow(10, zeroes - this.zeroes));
        } else {
            currentMoney *= Math.pow(10, this.zeroes - zeroes);
            currentMoney += moneyToAdd;
            this.zeroes = zeroes;
        }
        checkForCurrencyChange();
        mainActivity.updateCurrentMoney(currentMoney, this.zeroes);
    }

    public boolean hasEnoughMoney(double toPay, int toPayZeroes) {
        if (zeroes == toPayZeroes) {
            return currentMoney >= toPay;
        }
        if (zeroes > toPayZeroes) {
            return currentMoney * Math.pow(10, zeroes - toPayZeroes) > toPay;
        }
        return false;
    }


    public void sendNotificationAndSaveDoneTime(SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
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
        if (longestTimeUntilFinished >= 60000) {
            AlarmManagerUtil.setAlarmInTime(mainActivity.getApplicationContext(), longestTimeUntilFinished);
        }
    }

}