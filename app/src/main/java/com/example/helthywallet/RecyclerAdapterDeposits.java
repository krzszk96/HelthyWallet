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

public class RecyclerAdapterDeposits extends RecyclerView.Adapter<RecyclerAdapterDeposits.ViewHolder> {

    private static final String Tag = "RecyclerViewTransactions";
    private Context mContext;
    private ArrayList<DepositModel> modelList;
    private OnItemClickListener mListener;

    public RecyclerAdapterDeposits(Context mContext, ArrayList<DepositModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }
    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {mListener = listener;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        //widgets
        ImageView depositImg,deleteImg;
        TextView title,amount,time,interest;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.depositTitle);
            amount = itemView.findViewById(R.id.depositAmount);
            time = itemView.findViewById(R.id.timeDisplay);
            interest = itemView.findViewById(R.id.interestDisplay);
            depositImg = itemView.findViewById(R.id.depositIcon);
            deleteImg = itemView.findViewById(R.id.deleteDeposit);

            deleteImg.setOnClickListener(new View.OnClickListener() {
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
    public RecyclerAdapterDeposits.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deposit_item,parent, false );

        return new RecyclerAdapterDeposits.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterDeposits.ViewHolder holder, int position) {
        holder.title.setText(modelList.get(position).getTitle());
        holder.amount.setText(modelList.get(position).getAmount());
        holder.time.setText(modelList.get(position).getTime());
        holder.interest.setText(modelList.get(position).getInterest());
        holder.depositImg.setImageResource(modelList.get(position).getImg()); //here should be glide library
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
