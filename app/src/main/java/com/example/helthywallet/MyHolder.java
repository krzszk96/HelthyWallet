package com.example.helthywallet;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder {

    ImageView mImageView;
    TextView currencyValue, currencyWorthBefore, currencyWorthNow, profit, rate ,CurrencyName;

    public MyHolder(@NonNull View itemView) {
        super(itemView);

        this.CurrencyName = itemView.findViewById(R.id.CurrencyName);
        this.mImageView = itemView.findViewById(R.id.imageView551);
        this.rate = itemView.findViewById(R.id.rate);
        this.currencyValue = itemView.findViewById(R.id.CurrencyValueDisplay);
        this.currencyWorthBefore = itemView.findViewById(R.id.currencyWorthBefore);
        this.currencyWorthNow = itemView.findViewById(R.id.currencyWorthNow);
        this.profit = itemView.findViewById(R.id.profit);
    }
}
