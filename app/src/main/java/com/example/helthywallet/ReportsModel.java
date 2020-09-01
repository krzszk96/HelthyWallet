package com.example.helthywallet;

public class ReportsModel {

    private String transactionWallet, income, expenses, investWallet, deposits, currencyWallet, date , id;

    public ReportsModel(){

    }
    public ReportsModel(String transactionWallet, String income, String expenses, String investWallet, String deposits, String currencyWallet, String date, String id) {
        this.transactionWallet = transactionWallet;
        this.income = income;
        this.expenses = expenses;
        this.investWallet = investWallet;
        this.deposits = deposits;
        this.currencyWallet = currencyWallet;
        this.date = date;
        this.id = id;
    }

    public String getTransactionWallet() {
        return transactionWallet;
    }

    public void setTransactionWallet(String transactionWallet) {
        this.transactionWallet = transactionWallet;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getExpenses() {
        return expenses;
    }

    public void setExpenses(String expenses) {
        this.expenses = expenses;
    }

    public String getInvestWallet() {
        return investWallet;
    }

    public void setInvestWallet(String investWallet) {
        this.investWallet = investWallet;
    }

    public String getDeposits() {
        return deposits;
    }

    public void setDeposits(String deposits) {
        this.deposits = deposits;
    }

    public String getCurrencyWallet() {
        return currencyWallet;
    }

    public void setCurrencyWallet(String currencyWallet) {
        this.currencyWallet = currencyWallet;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
