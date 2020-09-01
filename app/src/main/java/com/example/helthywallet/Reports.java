package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;
import com.google.firebase.database.core.RepoInfo;

import java.util.ArrayList;

public class Reports extends AppCompatActivity {

    Button generateReport;
    ImageView menu_btn;
    DatabaseReference referenceSave, referenceRead;

    Animation scaleUp,scaleDown;

    RecyclerView recyclerView; //widget

    private RecyclerAdapterReports recyclerAdapterReports; //firebase

    private ArrayList<ReportsModel> modelsList; //variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        menu_btn = (ImageView) findViewById(R.id.image_menu);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_btn.startAnimation(scaleUp);
                menu_btn.startAnimation(scaleDown);
                startActivity(new Intent(Reports.this, MainScreen.class));
            }
        });

        recyclerView = findViewById(R.id.recyclerReports);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        generateReport = (Button) findViewById(R.id.generateReportBtn);
        generateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                generateReport.startAnimation(scaleUp);
                generateReport.startAnimation(scaleDown);

                AlertDialog.Builder builder = new AlertDialog.Builder(Reports.this);
                builder.setTitle("Generowanie raportu");
                builder.setMessage("UWAGA! Wszystkie transakcje zostaną wyzerowane, czy na pewno chcesz wygenerować raport?");

                builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNewReport();
                    }
                });
                builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();

            }
        });

        readReports();
    }
    public void removeReport(final int position){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        referenceRead = FirebaseDatabase.getInstance().getReference("users").child(ref).child("reports");

        referenceRead.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String checkId = modelsList.get(position).getId();

                referenceRead.child(checkId).removeValue();
                modelsList.remove(position);
                recyclerAdapterReports.notifyItemRemoved(position);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void readReports(){

        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        referenceRead = FirebaseDatabase.getInstance().getReference("users").child(ref).child("reports");

        referenceRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    ClearAll();
                    for (DataSnapshot children: dataSnapshot.getChildren()) {

                        String transactionWallet = children.child("wallet").getValue().toString();
                        String income = children.child("income").getValue().toString();
                        String expenses = children.child("expenses").getValue().toString();
                        String investWallet = children.child("investWallet").getValue().toString();
                        String deposits = children.child("deposits").getValue().toString();
                        String currencyWallet = children.child("currencyWallet").getValue().toString();
                        String dateNow = children.child("dateNow").getValue().toString();
                        String id = children.getKey();

                        ReportsModel report = new ReportsModel();
                        report.setTransactionWallet(transactionWallet);
                        report.setIncome(income);
                        report.setExpenses(expenses);
                        report.setInvestWallet(investWallet);
                        report.setDeposits(deposits);
                        report.setCurrencyWallet(currencyWallet);
                        report.setDate(dateNow);
                        report.setId(id);
                        modelsList.add(report);
                    }
                }catch (Exception e){}

                recyclerAdapterReports = new RecyclerAdapterReports(getApplicationContext(), modelsList);
                recyclerView.setAdapter(recyclerAdapterReports);
                recyclerAdapterReports.notifyDataSetChanged();

                recyclerAdapterReports.setOnItemClickListener(new RecyclerAdapterReports.OnItemClickListener() {
                    @Override
                    public void onDeleteClick(final int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Reports.this);
                        builder.setTitle("Usuń raport");
                        builder.setMessage("Czy na pewno chcesz usunąć wybrany raport?");

                        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeReport(position);
                            }
                        });
                        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create().show();
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }
    public void saveNewReport(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        referenceSave = FirebaseDatabase.getInstance().getReference("users").child(ref);

        referenceSave.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                try {
                    if (!dataSnapshot.hasChild("reports")){referenceSave.child("reports").setValue(0);}

                    String transactionWallet = dataSnapshot.child("wallet").getValue().toString();
                    String income = dataSnapshot.child("income").getValue().toString();
                    String expenses = dataSnapshot.child("expenses").getValue().toString();

                    String investWallet = dataSnapshot.child("investAcc").getValue().toString();
                    String deposits = dataSnapshot.child("totalDeposits").getValue().toString();
                    String currencyWallet = dataSnapshot.child("currencyWallet").getValue().toString();
                    String dateNow = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { //check if api version high enough
                        dateNow = String.valueOf(java.time.LocalDate.now());
                    }else{ dateNow = "00/00/00"; }

                    int count1=0;
                    for (DataSnapshot children : dataSnapshot.child("reports").getChildren()){
                        count1 =  Integer.parseInt(children.getKey());
                    }
                    count1++;

                    referenceSave.child("reports").child(Long.toString(count1)).child("wallet").setValue(transactionWallet);
                    referenceSave.child("reports").child(Long.toString(count1)).child("income").setValue(income);
                    referenceSave.child("reports").child(Long.toString(count1)).child("expenses").setValue(expenses);
                    referenceSave.child("reports").child(Long.toString(count1)).child("investWallet").setValue(investWallet);
                    referenceSave.child("reports").child(Long.toString(count1)).child("deposits").setValue(deposits);
                    referenceSave.child("reports").child(Long.toString(count1)).child("currencyWallet").setValue(currencyWallet);
                    referenceSave.child("reports").child(Long.toString(count1)).child("dateNow").setValue(dateNow);

                    referenceSave.child("categories").removeValue();

                }catch (Exception e){}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }
    private void ClearAll(){
        if (modelsList != null){
            modelsList.clear();

            if(recyclerAdapterReports != null){
                recyclerAdapterReports.notifyDataSetChanged();
            }
        }
        modelsList = new ArrayList<>();
    }
}
