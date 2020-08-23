package com.example.helthywallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter1 extends RecyclerView.Adapter<MyHolder> {

    Context c;
    ArrayList<Model> models; //this array list create a list of array which parameters define in our class

    public Adapter1(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, null); //this inflate our row
        return new MyHolder(view); //this will return our view holder class
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.currencyValue.setText(models.get(i).getName());
        myHolder.currencyValue.setText(Double.toString( models.get(i).getCurrencyValue()));
        myHolder.rate.setText(Double.toString( models.get(i).getRate()));
        myHolder.currencyWorthBefore.setText(Double.toString(models.get(i).getCurrencyWorthBefore()));
        myHolder.currencyWorthNow.setText(Double.toString(models.get(i).getCurrencyWorthNow()));
        myHolder.profit.setText(Double.toString(models.get(i).getProfit()));
        myHolder.mImageView.setImageResource(models.get(i).getImg());
    }
    @Override
    public int getItemCount() {
        return models.size();
    }
}
