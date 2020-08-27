package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class MainViewChart extends AppCompatActivity {

    EditText inAmount, inTitle, newCat, inAmountV, inDateD, inDateM, inDateY;
    Button addTransaction, addData;
    Spinner spinCats;
    ImageView menu_btn;

    DatabaseReference reference;
    ValueEventListener listener;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDatalist;

    Animation scaleUp,scaleDown;

    //widget
    RecyclerView recyclerView;

    //firebase
    private RecyclerAdapterTransactions recyclerAdapterTransactions;

    //variables
    private ArrayList<TransactionModel> modelsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_chart);

        menu_btn = (ImageView) findViewById(R.id.image_menu);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainViewChart.this, MainScreen.class));
            }
        });

        inAmountV = (EditText) findViewById(R.id.addAmountV);
        inAmountV.setText("-");
        inAmount = (EditText) findViewById(R.id.addAmount);
        inTitle = (EditText) findViewById(R.id.addTitle);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        recyclerView = findViewById(R.id.recyclerTransactions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        displayData2();

        addTransaction = (Button) findViewById(R.id.addCatBtn);
        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction.startAnimation(scaleUp);
                addTransaction.startAnimation(scaleDown);
                addCategroy();
            }
        });

        spinCats = (Spinner) findViewById(R.id.categorySpinner);
        spinnerDatalist = new ArrayList<>();
        adapter = new ArrayAdapter<String>(MainViewChart.this, android.R.layout.simple_spinner_dropdown_item, spinnerDatalist);
        spinCats.setAdapter(adapter);
        retrieveData();

        addData = (Button) findViewById(R.id.addDataBtn);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData.startAnimation(scaleUp);
                addData.startAnimation(scaleDown);
                addTransaction();
            }
        });



    }
    public void addCategroy(){

        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref);

        newCat = (EditText) findViewById(R.id.addCategoryBase);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reference.child("categories").child(newCat.getText().toString()).setValue("0");
                newCat.setText("");
                recreate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void retrieveData(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("categories");
        listener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item:dataSnapshot.getChildren()){
                    spinnerDatalist.add(item.getKey());
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public boolean dataCheck(){
        inDateD = (EditText) findViewById(R.id.addDateD);
        inDateM = (EditText) findViewById(R.id.addDateM);
        inDateY = (EditText) findViewById(R.id.addDateY);
        int day, month, year;
        boolean result = false;

        day = Integer.valueOf(inDateD.getText().toString());
        month = Integer.valueOf(inDateM.getText().toString());
        year = Integer.valueOf(inDateY.getText().toString());

        if(day <= 31 && day > 0 ){
            if(month <= 12 && month > 0 ) {
                if(year < 2500 && year > 2019 ) result = true;
            }
        }
        return result;
    }
    public void addTransaction(){

        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String cat = spinCats.getSelectedItem().toString();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("categories").child(cat);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long count= dataSnapshot.getChildrenCount();
                String amount = inAmountV.getText().toString() + inAmount.getText().toString();

                if(dataCheck()) {
                    String date = inDateD.getText().toString() + "/" + inDateM.getText().toString() + "/" + inDateY.getText().toString();
                    reference.child(Long.toString(count)).child("data").setValue(date);
                    reference.child(Long.toString(count)).child("tytul").setValue(inTitle.getText().toString());
                    reference.child(Long.toString(count)).child("kwota").setValue(amount);
                    inTitle.setText("");
                    inAmount.setText("");
                    inDateD.setText(""); inDateM.setText(""); inDateY.setText("");
                }else{
                    Toast.makeText(MainViewChart.this, "zły format daty!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public void displayData2(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("categories");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    ClearAll();
                    for (DataSnapshot children : dataSnapshot.getChildren()) {

                        long count = children.getChildrenCount();
                        for (int i = 0; i < count; i++) {
                            String category = children.getKey();
                            String title = children.child(Integer.toString(i)).child("tytul").getValue().toString();
                            String amount = children.child(Integer.toString(i)).child("kwota").getValue().toString();
                            String date = children.child(Integer.toString(i)).child("data").getValue().toString();

                            TransactionModel transaction = new TransactionModel();
                            transaction.setCategory(category);
                            transaction.setTitle(title);
                            transaction.setAmount(amount);
                            transaction.setDate(date);
                            transaction.setImg(R.drawable.home_icon);
                            modelsList.add(transaction);

                        }
                    }
                }catch (Exception e){}
                recyclerAdapterTransactions = new RecyclerAdapterTransactions(getApplicationContext(), modelsList);
                recyclerView.setAdapter(recyclerAdapterTransactions);
                recyclerAdapterTransactions.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    private void ClearAll(){
        if (modelsList != null){
            modelsList.clear();

            if(recyclerAdapterTransactions != null){
                recyclerAdapterTransactions.notifyDataSetChanged();
            }
        }
        modelsList = new ArrayList<>();
    }
}
