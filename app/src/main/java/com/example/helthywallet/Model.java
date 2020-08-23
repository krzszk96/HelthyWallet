package com.example.helthywallet;

public class Model {

    private String name;
    private double currencyValue, currencyWorthBefore, currencyWorthNow, profit, rate;
    private int img;

    public double getCurrencyValue() {
        return currencyValue;
    }
    public void setCurrencyValue(double currencyValue) {
        this.currencyValue = currencyValue;
    }

    public double getCurrencyWorthBefore() {
        return currencyWorthBefore;
    }
    public void setCurrencyWorthBefore(double currencyWorthBefore) {
        this.currencyWorthBefore = currencyWorthBefore;
    }

    public double getCurrencyWorthNow() {
        return currencyWorthNow;
    }
    public void setCurrencyWorthNow(double currencyWorthNow) {
        this.currencyWorthNow = currencyWorthNow;
    }

    public double getProfit() {
        return profit;
    }
    public void setProfit(double profit) {
        this.profit = profit;
    }

    public int getImg() {
        return img;
    }
    public void setImg(int img) {
        this.img = img;
    }

    public double getRate() {
        return rate;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
