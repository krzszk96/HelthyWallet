package com.example.helthywallet;

public class DepositModel {

    private String title, amount, time, interest;
    int img;

    public DepositModel(){

    }

    public DepositModel(String title, String amount, String time, String interest, int img) {
        this.title = title;
        this.amount = amount;
        this.time = time;
        this.interest = interest;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
