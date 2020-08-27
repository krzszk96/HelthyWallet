package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;


public class Investments extends AppCompatActivity {

    TextView curRat,topUpShow;
    private RequestQueue mQueue;
    ArrayAdapter<String> adapter;
    Spinner ratesSpinner;
    ImageView menu_btn;
    //RecyclerView mRecyclerview;
    //Adapter1 myAdapter;
    Button buyCurrency, chargeBtn, withdrawBtn;
    EditText entValue, entCurrencyName, entCurrencyRate, chargeAccountVal;

    //widget
    RecyclerView recyclerView;

    //firebase
    private DatabaseReference currencyReference;
    private RecyclerAdapter recyclerAdapter;
    private Context mContext;

    //variables
    private ArrayList<Model> modelsList;

    DatabaseReference reference,reference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investments);

        menu_btn = (ImageView) findViewById(R.id.image_menu);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Investments.this, MainScreen.class));
            }
        });

        mQueue = Volley.newRequestQueue(this);
        jsonRates();

        recyclerView = findViewById(R.id.recyclerCurr);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //firebase
        //currencyReference = FirebaseDatabase.getInstance().getReference();

        //Arraylist
        modelsList = new ArrayList<>();
        //clear arraylist
        ClearAll();
        //get data method
        GetDataFromFirebase();

        buyCurrency = (Button) findViewById(R.id.buyCurrencyBtn);
        buyCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyCurrency();
            }
        });

        updateAccount();

        chargeBtn = (Button) findViewById(R.id.chargeBtn);
        chargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topUpAccount();
            }
        });
        withdrawBtn = (Button) findViewById(R.id.withdrawbtn);
        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawAccount();
            }
        });

    }
    private void GetDataFromFirebase(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currencyReference = FirebaseDatabase.getInstance().getReference("users").child(ref);

        Query query = currencyReference.child("currencytrasactions");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Model model = new Model();
                    model.setName(snapshot.child("nazwa").getValue().toString());
                    model.setCurrencyValue(Double.parseDouble(snapshot.child("kwota").getValue().toString()));
                    model.setCurrencyWorthBefore(500.00);
                    model.setCurrencyWorthNow(600.00);
                    model.setProfit(100.00);
                    model.setRate(Double.parseDouble(snapshot.child("kurs").getValue().toString()));
                    model.setImg(R.drawable.transaction_icon);

                    modelsList.add(model);
                }
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(), modelsList);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void ClearAll(){
        if (modelsList != null){
            modelsList.clear();

            if(recyclerAdapter != null){
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        modelsList = new ArrayList<>();
    }

    private void updateAccount(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference1 = FirebaseDatabase.getInstance().getReference("users").child(ref);

        topUpShow = (TextView) findViewById(R.id.accBalance);

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("investAcc")) {
                    String valueformdatabase = dataSnapshot.child("investAcc").getValue().toString();
                    topUpShow.setText(valueformdatabase);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    private void topUpAccount(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference1 = FirebaseDatabase.getInstance().getReference("users").child(ref).child("investAcc");

        chargeAccountVal = (EditText) findViewById(R.id.chargeAccount);
        topUpShow = (TextView) findViewById(R.id.accBalance);

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    reference1.setValue(topUpShow.getText().toString());
                    double investAccBalance = Double.parseDouble(topUpShow.getText().toString());
                    double operation = Double.parseDouble(chargeAccountVal.getText().toString());
                    investAccBalance = investAccBalance + operation;
                    topUpShow.setText(Double.toString(investAccBalance));
                    reference1.setValue(topUpShow.getText().toString());
                    chargeAccountVal.setText("");

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    private void withdrawAccount(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference1 = FirebaseDatabase.getInstance().getReference("users").child(ref).child("investAcc");

        chargeAccountVal = (EditText) findViewById(R.id.chargeAccount);
        topUpShow = (TextView) findViewById(R.id.accBalance);

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                double investAccBalance = Double.parseDouble(topUpShow.getText().toString());
                if(investAccBalance>0) {
                    double operation = Double.parseDouble(chargeAccountVal.getText().toString());
                    if(operation>investAccBalance){
                        Toast.makeText(Investments.this, "Za mało środków na konice", Toast.LENGTH_LONG).show();
                    }else{
                        investAccBalance = investAccBalance - operation;
                        topUpShow.setText(Double.toString(investAccBalance));
                        reference1.setValue(topUpShow.getText().toString());
                        chargeAccountVal.setText("");
                    }

                }else{
                    Toast.makeText(Investments.this, "Brak środków do wypłacenia", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    private void buyCurrency(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("currencytrasactions");

        entValue = (EditText) findViewById(R.id.enterCurrencyValue);
        entCurrencyName = (EditText) findViewById(R.id.enterCurrency);
        entCurrencyRate = (EditText) findViewById(R.id.enterCurrencyRate);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long count= dataSnapshot.getChildrenCount();

                 reference.child(Long.toString(count)).child("kwota").setValue(entValue.getText().toString());
                 reference.child(Long.toString(count)).child("nazwa").setValue(entCurrencyName.getText().toString());
                 reference.child(Long.toString(count)).child("kurs").setValue(entCurrencyRate.getText().toString());
                 entValue.setText("");
                 entCurrencyName.setText("");
                 entCurrencyRate.setText("");

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    private static DecimalFormat df2 = new DecimalFormat("#.####");
    private void jsonRates() {

        curRat = (TextView) findViewById(R.id.curRate);
        ratesSpinner = (Spinner) findViewById(R.id.rateSpinner);

        ArrayList<String> spinRdata = new ArrayList<>( Arrays.asList("CAD","HKD","ISK","PHP","DKK","HUF","CZK","AUD","RON","SEK","IDR","INR","BRL","RUB","HRK","JPY","THB","CHF","SGD","PLN","BGN","TRY","CNY","NOK","NZD","ZAR","USD","MXN","ILS","GBP","KRW","MYR"));
        adapter = new ArrayAdapter<>(Investments.this, android.R.layout.simple_spinner_dropdown_item, spinRdata);
        ratesSpinner.setAdapter(adapter);

        ratesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String url = "https://api.exchangeratesapi.io/latest";
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject rate = response.getJSONObject("rates");
                            String spinVal =  ratesSpinner.getSelectedItem().toString();
                            String rateToDisplay = String.valueOf(rate.get(spinVal));
                            String pln = String.valueOf(rate.get("PLN"));
                            double toPLN = Double.parseDouble(pln) / Double.parseDouble(rateToDisplay);
                            String display = String.valueOf(df2.format(toPLN));

                            curRat.setText(display);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                mQueue.add(request);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
}


