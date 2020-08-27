package com.example.helthywallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapterWallet extends RecyclerView.Adapter<RecyclerAdapterWallet.ViewHolder> {

    private static final String Tag = "RecyclerViewWallet";
    private Context mContext;
    private ArrayList<WalletModel> modelList;

    public RecyclerAdapterWallet(Context mContext, ArrayList<WalletModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //widgets
        ImageView catImage;
        TextView amount,percent,category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.catDisplay);
            amount = itemView.findViewById(R.id.displayAmount);
            percent = itemView.findViewById(R.id.displayPercent);
            catImage = itemView.findViewById(R.id.catImage);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterWallet.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_item,parent, false );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterWallet.ViewHolder holder, int position) {
        holder.category.setText(modelList.get(position).getCategory());
        holder.amount.setText(String.valueOf(modelList.get(position).getAmount()));
        holder.percent.setText(String.valueOf(modelList.get(position).getPercent()));
        holder.catImage.setImageResource(modelList.get(position).getImg()); //here should be glide library
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
