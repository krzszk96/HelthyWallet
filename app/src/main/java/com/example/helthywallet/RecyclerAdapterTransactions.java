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

public class RecyclerAdapterTransactions extends RecyclerView.Adapter<RecyclerAdapterTransactions.ViewHolder> {

    private static final String Tag = "RecyclerViewTransactions";
    private Context mContext;
    private ArrayList<TransactionModel> modelList;

    public RecyclerAdapterTransactions(Context mContext, ArrayList<TransactionModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //widgets
        ImageView categoryImg;
        TextView category,title,amount,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.categoryDisplay);
            title = itemView.findViewById(R.id.titleDisplay);
            amount = itemView.findViewById(R.id.amountDisplay);
            date = itemView.findViewById(R.id.dateDisplay);
            categoryImg = itemView.findViewById(R.id.categoryImg);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterTransactions.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item,parent, false );

        return new RecyclerAdapterTransactions.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterTransactions.ViewHolder holder, int position) {
        holder.category.setText(modelList.get(position).getCategory());
        holder.title.setText(modelList.get(position).getTitle());
        holder.amount.setText(modelList.get(position).getAmount());
        holder.date.setText(modelList.get(position).getDate());
        holder.categoryImg.setImageResource(modelList.get(position).getImg()); //here should be glide library
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
