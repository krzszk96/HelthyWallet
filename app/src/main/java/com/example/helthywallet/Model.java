package com.example.helthywallet;

public class Model { //this is model class for investments recyclerview

    private String name, id, currencyValue, currencyWorthBefore, rate;
    private int img;

    public Model(){

    }

    public Model(String name,String currencyValue,String currencyWorthBefore,String rate,int img, String id){
        this.name = name;
        this.currencyValue = currencyValue;
        this.currencyWorthBefore = currencyWorthBefore;
        this.rate = rate;
        this.img = img;
        this.id = id;
    }

    public String getCurrencyValue() {
        return currencyValue;
    }
    public void setCurrencyValue(String currencyValue) {
        this.currencyValue = currencyValue;
    }

    public String getCurrencyWorthBefore() {
        return currencyWorthBefore;
    }
    public void setCurrencyWorthBefore(String currencyWorthBefore) {
        this.currencyWorthBefore = currencyWorthBefore;
    }

    public int getImg() {
        return img;
    }
    public void setImg(int img) {
        this.img = img;
    }

    public String getRate() {
        return rate;
    }
    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
