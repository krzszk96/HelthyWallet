package com.example.helthywallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mContext;
    private ArrayList<Model> modelList;
    private RecyclerAdapter.OnItemClickListener mListener;

    public RecyclerAdapter(Context mContext, ArrayList<Model> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }
    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(RecyclerAdapter.OnItemClickListener listener) {mListener = listener;}


    public class ViewHolder extends RecyclerView.ViewHolder{
        //widgets
        Button sellBtn;
        ImageView mImageView;
        TextView currencyValue, currencyWorthBefore, currencyWorthNow, profit, rate ,CurrencyName;

        public ViewHolder(@NonNull View itemView, final RecyclerAdapter.OnItemClickListener listener) {
            super(itemView);

            CurrencyName = itemView.findViewById(R.id.CurrencyName);
            currencyValue = itemView.findViewById(R.id.CurrencyValueDisplay);
            currencyWorthBefore = itemView.findViewById(R.id.currencyWorthBefore);
            currencyWorthNow = itemView.findViewById(R.id.currencyWorthNow);
            profit = itemView.findViewById(R.id.profit);
            rate = itemView.findViewById(R.id.rate);
            mImageView = itemView.findViewById(R.id.imageView551);
            sellBtn = itemView.findViewById(R.id.sellBtn);

            sellBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_item,parent, false );

        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.CurrencyName.setText(modelList.get(position).getName());
        holder.currencyValue.setText(String.valueOf(modelList.get(position).getCurrencyValue()));
        holder.currencyWorthBefore.setText(String.valueOf(modelList.get(position).getCurrencyWorthBefore()));
        holder.currencyWorthNow.setText(String.valueOf(modelList.get(position).getCurrencyWorthNow()));
        holder.profit.setText(String.valueOf(modelList.get(position).getProfit()));
        holder.rate.setText(String.valueOf(modelList.get(position).getRate()));
        holder.mImageView.setImageResource(modelList.get(position).getImg()); //here should be glide library
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
