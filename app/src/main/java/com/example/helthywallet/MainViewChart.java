package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class MainViewChart extends AppCompatActivity {

    EditText inAmount, inTitle, inDate, newCat;
    TextView seeData;
    Button addTransaction, addData;
    Spinner spinCats;

    DatabaseReference reference;
    ValueEventListener listener;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDatalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_chart);

        inAmount = (EditText) findViewById(R.id.addAmount);
        inTitle = (EditText) findViewById(R.id.addTitle);
        inDate = (EditText) findViewById(R.id.addDate);



        addTransaction = (Button) findViewById(R.id.addCatBtn);
        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    public void addTransaction(){

        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String cat = spinCats.getSelectedItem().toString();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("categories").child(cat);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String tran = "T: " + inTitle.getText().toString() + " -$: " + inAmount.getText().toString() + " -D: " + inDate.getText().toString();
                long count= dataSnapshot.getChildrenCount();
                reference.child(Long.toString(count)).setValue(tran);
                recreate();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public void displayData(){ //not workin yet, need to add displaying multiple categories
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("categories").child("praca");
        seeData = (TextView) findViewById(R.id.viewData);


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count= dataSnapshot.getChildrenCount();
                String buff = "";
                for(int i=0; i<count; i++){
                    //String display = dataSnapshot.getValue();
                    //buff = buff + display;
                }
                seeData.setText(buff);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
