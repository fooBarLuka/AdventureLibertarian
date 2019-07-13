package com.example.adventurelibertarian;

import android.content.SharedPreferences;
import android.os.SystemClock;

import com.example.adventurelibertarian.Activities.MainActivity;
import com.example.adventurelibertarian.models.Factory;
import com.example.adventurelibertarian.utils.SharedPreferencesConstants;

import java.util.List;

public class MainActivityPresenter implements SharedPreferencesConstants {
    public int zeroes = 0;
    public double currentMoney = 0;

    private double offlineMoney = 0;
    private int offlineMoneyZeroes = 0;

    private MainActivity mainActivity;

    public MainActivityPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void loadFactories(List<Factory> factories, SharedPreferences preferences) {
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
        for (int i = 0; i < mainActivity.factories.size(); i++) {
            long millisDone = preferences.getLong(i + "done", 0);
            mainActivity.factories.get(i).loadFactoryProgress(currentTime, prevTime, millisDone);
        }
        sumMoney(offlineMoney, offlineMoneyZeroes);
        mainActivity.showAfterIdleDialog(offlineMoney, offlineMoneyZeroes);
    }

    public void getBonusForTime(long time, List<Factory> factories) {
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

    public void saveInSharedPreferences(List<Factory> factories, boolean delete) {
        SharedPreferences.Editor editor = mainActivity.getSharedPreferences(SHARED_PREFERENCES_NAME, mainActivity.MODE_PRIVATE).edit();
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

}