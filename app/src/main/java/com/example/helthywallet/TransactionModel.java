package com.example.helthywallet;

public class TransactionModel {

    public String category, title, date, amount, id;
    public int img;

    public TransactionModel(){

    }

    public TransactionModel(String category, String title, String date, String amount,int img, String id ) {
        this.category = category;
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.img = img;
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
