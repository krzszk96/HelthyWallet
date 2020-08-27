package com.example.helthywallet;

public class WalletModel {

    private String category,amount;
    private double percent;
    private int img;

    public WalletModel() {
    }

    public WalletModel(String category, String amount, double percent, int img) {
        this.category = category;
        this.amount = amount;
        this.percent = percent;
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
