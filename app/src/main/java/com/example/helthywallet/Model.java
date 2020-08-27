package com.example.helthywallet;

public class Model { //this is model class for investments recyclerview

    private String name;
    private double currencyValue, currencyWorthBefore, currencyWorthNow, profit, rate;
    private int img;

    public Model(){

    }

    public Model(String name,double currencyValue,double currencyWorthBefore,double currencyWorthNow,double profit,double rate,int img){
        this.name = name;
        this.currencyValue = currencyValue;
        this.currencyWorthBefore = currencyWorthBefore;
        this.currencyWorthNow = currencyWorthNow;
        this.profit = profit;
        this.rate = rate;
        this.img = img;
    }

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
