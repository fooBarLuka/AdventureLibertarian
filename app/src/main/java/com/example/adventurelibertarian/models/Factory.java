package com.example.adventurelibertarian.models;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.adventurelibertarian.presenter.MainActivityPresenter;
import com.example.adventurelibertarian.utils.CountDownUtil;
import com.example.adventurelibertarian.utils.MyCountDownTimer;

public class Factory {

    public Factory(int id, double income, int zeroes, double incomeMultiplier,
                   double upgradePrice, int upgradeZeroes, double upgradePriceMultiplier,
                   long waitingTime, int imageId, MainActivityPresenter mainActivityPresenter) {
        this.id = id;
        this.income = income;
        this.zeroes = zeroes;
        this.incomeMultiplier = incomeMultiplier;
        this.upgradePrice = upgradePrice;
        this.upgradeZeroes = upgradeZeroes;
        this.upgradePriceMultiplier = upgradePriceMultiplier;
        this.waitingTime = waitingTime;
        this.imageId = imageId;
        this.mainActivityPresenter = mainActivityPresenter;
    }

    public void fullLevelUp() {
        income *= incomeMultiplier;
        upgradePrice *= upgradePriceMultiplier;
        level++;
        checkCurrencyChange();
    }

    public void checkCurrencyChange() {
        if (upgradePrice > 1000000) {
            upgradeZeroes += 3;
            upgradePrice /= 1000;
        }
        if (income > 1000000) {
            income /= 1000;
            zeroes += 3;
        }
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public void factoryEvolution() {
        income *= 10;
        incomeMultiplier = (incomeMultiplier - 1) * 2 / 3 + 1;
        checkCurrencyChange();
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean state) {
        working = state;
    }


    public long getWaitingTime() {
        return waitingTime;
    }

    public double getUpgradePrice() {
        return upgradePrice;
    }

    public void setUpgradePrice(double upgradePrice) {
        this.upgradePrice = upgradePrice;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void increaseLevel() {
        level++;
    }


    public double getUpgradePriceMultiplier() {
        return upgradePriceMultiplier;
    }

    public void setUpgradePriceMultiplier(double upgradePriceMultiplier) {
        this.upgradePriceMultiplier = upgradePriceMultiplier;
    }

    public double getIncomeMultiplier() {
        return incomeMultiplier;
    }

    public void setIncomeMultiplier(double incomeMultiplier) {
        this.incomeMultiplier = incomeMultiplier;
    }

    public int getUpgradeZeros() {
        return upgradeZeroes;
    }

    public void setUpgradeZeros(int zeroes) {
        this.upgradeZeroes = zeroes;
    }

    public int getZeroes() {
        return zeroes;
    }

    public void setZeroes(int zeros) {
        this.zeroes = zeros;
    }

    public void setHasManager(boolean hasManager) {
        this.hasManager = hasManager;
    }

    public boolean getHasManager() {
        return hasManager;
    }

    public int getImageId() {
        return imageId;
    }

    public int getId() {
        return id;
    }

    public long getMillisUntilFinished() {
        return millisUntilFinished;
    }

    public void onTimePassed(long timePassed){
        millisUntilFinished -= timePassed;
        if(millisUntilFinished < 0){
            millisUntilFinished = waitingTime;
            mainActivityPresenter.sumMoney(income, zeroes);
            countDownUtil = new CountDownUtil(waitingTime);
            if(!hasManager){
                MyCountDownTimer.removeFactory(this);
            }
            working = false;
        } else {
            countDownUtil.onTickHappened(timePassed);

            if (factoryProgressBar != null) {
                factoryProgressBar.setProgress((int) (100 * (waitingTime - millisUntilFinished) / waitingTime));
            }

            if(timeLeftTextView != null){
                timeLeftTextView.setText(countDownUtil.getMinutes() + ":" + countDownUtil.getSeconds() + ":" + countDownUtil.getMilliSeconds());
            }
        }
    }

    public void hireManager() {
        hasManager = true;
        if (open && !working) {
            working = true;
            MyCountDownTimer.addFactory(this);
        }
    }

    public void loadFactoryProgress(long timePassed,long millisDone) {
        if (isOpen() && millisDone != 0) {
            long fullTime = millisDone + timePassed;
            if (fullTime < getWaitingTime()) {
                countDownUtil = new CountDownUtil(waitingTime - fullTime);
                working = true;
                MyCountDownTimer.addFactory(this);
            } else {
                if (getHasManager()) {
                    int times = (int) (fullTime / getWaitingTime());
                    long progressToSet = fullTime % getWaitingTime();
                    mainActivityPresenter.sumOfflineMoney(getIncome() * times, getZeroes());

                    countDownUtil = new CountDownUtil(waitingTime - progressToSet);
                    working = true;
                    MyCountDownTimer.addFactory(this);
                } else {
                    mainActivityPresenter.sumOfflineMoney(getIncome(), getZeroes());
                }
            }
        }
    }

    public boolean upgradeFactory() {
        double upgradePrice = getUpgradePrice();
        int upgradeZeroes = getUpgradeZeros();
        if (mainActivityPresenter.hasEnoughMoney(upgradePrice, upgradeZeroes)) {
            if (!isOpen()) {
                setOpen(true);
                increaseLevel();
            } else {
                if (getLevel() % 25 == 24) {
                    if (getLevel() % 100 == 99) {
                        factoryEvolution();
                    }
                    factoryEvolution();
                }
                fullLevelUp();
            }

            mainActivityPresenter.subtractPrice(upgradePrice, upgradeZeroes);
            return true;
        }
        return false;
    }

    public void setFactoryProgressBar(ProgressBar factoryProgressBar) {
        this.factoryProgressBar = factoryProgressBar;
    }

    public void setTimeLeftTextView(TextView timeLeftTextView) {
        this.timeLeftTextView = timeLeftTextView;
    }

    private int id;
    private int imageId;
    private double income;
    private double upgradePriceMultiplier;
    private double incomeMultiplier;
    private double upgradePrice;
    private long waitingTime;
    private long millisUntilFinished;
    private boolean open = false;
    private boolean working = false;
    private boolean hasManager = false;
    private int level = 0;
    private int upgradeZeroes;
    private int zeroes;

    private CountDownUtil countDownUtil;

    private ProgressBar factoryProgressBar;
    private TextView timeLeftTextView;
    private MainActivityPresenter mainActivityPresenter;

}
