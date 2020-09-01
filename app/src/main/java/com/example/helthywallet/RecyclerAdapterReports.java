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

public class RecyclerAdapterReports extends RecyclerView.Adapter<RecyclerAdapterReports.ViewHolder>{

    private static final String Tag = "RecyclerViewReports";
    private Context mContext;
    private ArrayList<ReportsModel> modelList;
    private RecyclerAdapterReports.OnItemClickListener mListener;

    public RecyclerAdapterReports(Context mContext, ArrayList<ReportsModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }
    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(RecyclerAdapterReports.OnItemClickListener listener) {mListener = listener;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        //widgets
        ImageView deleteImg;
        TextView transactionWallet,income,expenses,investWallet,deposits,currencyWallet,reportDate;

        public ViewHolder(@NonNull View itemView, final RecyclerAdapterReports.OnItemClickListener listener) {
            super(itemView);

            transactionWallet = itemView.findViewById(R.id.mainWallet);
            income = itemView.findViewById(R.id.income);
            expenses = itemView.findViewById(R.id.expense);
            investWallet = itemView.findViewById(R.id.investWallet);
            deposits = itemView.findViewById(R.id.deposits);
            currencyWallet = itemView.findViewById(R.id.currencies);
            reportDate = itemView.findViewById(R.id.reportDate);
            deleteImg = itemView.findViewById(R.id.deleteReport);

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
    public RecyclerAdapterReports.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item,parent, false );

        return new RecyclerAdapterReports.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterReports.ViewHolder holder, int position) {
        holder.transactionWallet.setText(modelList.get(position).getTransactionWallet());
        holder.income.setText(modelList.get(position).getIncome());
        holder.expenses.setText(modelList.get(position).getExpenses());
        holder.investWallet.setText(modelList.get(position).getInvestWallet());
        holder.deposits.setText(modelList.get(position).getDeposits());
        holder.reportDate.setText(modelList.get(position).getDate());
        holder.currencyWallet.setText(modelList.get(position).getCurrencyWallet());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}

