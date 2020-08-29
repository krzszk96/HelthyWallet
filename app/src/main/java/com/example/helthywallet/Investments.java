package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Investments extends AppCompatActivity {

    TextView curRat,topUpShow, currencyAccount;
    private RequestQueue mQueue;
    ArrayAdapter<String> adapter;
    Spinner ratesSpinner;
    ImageView menu_btn;
    Button buyCurrency, chargeBtn, withdrawBtn;
    EditText entValue, entCurrencyName, entCurrencyRate, chargeAccountVal;
    double toPassRate;

    //widget
    RecyclerView recyclerView;

    //firebase
    private DatabaseReference currencyReference;
    private RecyclerAdapter recyclerAdapter;

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

        buyCurrency = (Button) findViewById(R.id.buyCurrencyBtn);
        buyCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyCurrency();
            }
        });

        recyclerView = findViewById(R.id.recyclerCurr);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Arraylist
        modelsList = new ArrayList<>();
        //clear arraylist
        ClearAll();
        //get data method
        GetDataFromFirebase();

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
    public boolean checkConditions(){
        String name = entCurrencyName.getText().toString();
        String value = entValue.getText().toString();
        String rate = entCurrencyRate.getText().toString();

        if(name.equals("")){Toast.makeText(Investments.this, "Wpisz tytuł", Toast.LENGTH_SHORT).show(); return false;}
        if(value.equals("")){Toast.makeText(Investments.this, "Wpisz znak + lub -", Toast.LENGTH_SHORT).show(); return false;}
        if(rate.equals("")){Toast.makeText(Investments.this, "Wpisz kwotę transakcji", Toast.LENGTH_SHORT).show(); return false;}

        if(name.length()>3){Toast.makeText(Investments.this, "Zły format nazwy, maks 3 znaki", Toast.LENGTH_SHORT).show(); return false;}
        ArrayList<String> checkName = new ArrayList<>( Arrays.asList("CAD","HKD","ISK","PHP","DKK","HUF","CZK","AUD","RON","SEK","IDR","INR","BRL","RUB","HRK","JPY",
                "THB","CHF","SGD","PLN","BGN","TRY","CNY","NOK","NZD","ZAR","USD","MXN","ILS","GBP","KRW","MYR"));
        if(!checkName.contains(name)){Toast.makeText(Investments.this, "Niestety nie ma takiej waluty w bazie!", Toast.LENGTH_SHORT).show(); return false;};
        if(!isNumeric(value)){Toast.makeText(Investments.this, "Uwaga tekst zamiast kwoty!", Toast.LENGTH_SHORT).show(); return false;}
        if(!isNumeric(rate)){Toast.makeText(Investments.this, "Uwaga tekst zamiast kwoty!", Toast.LENGTH_SHORT).show(); return false;}

        return true;
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    public void sellItem(final int position, final String rate){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("currencytrasactions");
        topUpShow = (TextView) findViewById(R.id.accBalance);
        currencyAccount = (TextView) findViewById(R.id.currencyWallet);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String checkId = modelsList.get(position).getId();

                double amount = Double.parseDouble(dataSnapshot.child(checkId).child("kwota").getValue().toString());
                double rateToD = Double.parseDouble(rate);
                double boughtValue = modelsList.get(position).getCurrencyWorthBefore();

                double calculateValue = amount * rateToD;
                double calculateProfit = boughtValue - calculateValue;
                Toast.makeText(Investments.this, "Profit: " + calculateProfit, Toast.LENGTH_LONG).show();

                double accValue = Double.parseDouble(topUpShow.getText().toString());
                double accCurrencyValue = Double.parseDouble(currencyAccount.getText().toString());

                double updateInves = accValue + calculateValue;
                double updateCurrency = accCurrencyValue - boughtValue;
                reference1.child("investAcc").setValue(String.valueOf(df2.format(updateInves)));
                reference1.child("currencyWallet").setValue(String.valueOf(df2.format(updateCurrency)));

                updateAccount();

                reference.child(checkId).removeValue();
                modelsList.remove(position);
                recyclerAdapter.notifyItemRemoved(position);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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
                try {
                    ClearAll();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.child("nazwa").getValue().toString();
                        double amount = Double.parseDouble(snapshot.child("kwota").getValue().toString());
                        double rate = Double.parseDouble(snapshot.child("kurs").getValue().toString());
                        double worthBefore = amount * rate;
                        String fixValue = df2.format(worthBefore);
                        String fixRate = df2.format(rate);
                        String id = snapshot.getKey();

                        Model model = new Model();
                        model.setName(name);
                        model.setCurrencyValue(Double.parseDouble(snapshot.child("kwota").getValue().toString()));
                        model.setCurrencyWorthBefore(Double.parseDouble(fixValue));
                        model.setCurrencyWorthNow(0.00);
                        model.setProfit(0.00);
                        model.setRate(Double.parseDouble(fixRate));
                        model.setId(id);
                        model.setImg(R.drawable.transaction_icon);

                        modelsList.add(model);
                    }
                }catch (Exception e){}
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(), modelsList);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();

                recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onDeleteClick(final int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Investments.this);
                        builder.setTitle("Sprzedawanie waluty");
                        builder.setMessage("Wprowadź kurs sprzedaży waluty");
                        final EditText input = new EditText(Investments.this);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        builder.setView(input);

                        builder.setPositiveButton("Sprzedaj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String rate = input.getText().toString();
                                if(rate.equals("")){Toast.makeText(Investments.this, "wprowadź kurs", Toast.LENGTH_LONG).show();}
                                if(!rate.equals("")){sellItem(position, rate);}
                            }
                        });
                        builder.setNegativeButton("Wróć", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.create().show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "error");
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
        currencyAccount = (TextView) findViewById(R.id.currencyWallet);

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("investAcc")) {
                    try {
                        String valueformdatabase = dataSnapshot.child("investAcc").getValue().toString();
                        topUpShow.setText(valueformdatabase);
                    }catch (Exception e){}
                }else{
                    reference1.child("investAcc").setValue(0);
                }
                if (dataSnapshot.hasChild("currencyWallet")){
                    try {
                        String valueCur = dataSnapshot.child("currencyWallet").getValue().toString();
                        currencyAccount.setText(valueCur);
                    }catch (Exception e){}
                }else{
                    reference1.child("currencyWallet").setValue(0);
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
                    reference1.setValue(df2.format(topUpShow.getText().toString()));
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
        topUpShow = (TextView) findViewById(R.id.accBalance);
        currencyAccount = (TextView) findViewById(R.id.currencyWallet);


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int count1=0;
                for (DataSnapshot children : dataSnapshot.getChildren()){
                    count1 =  Integer.parseInt(children.getKey());
                }
                count1++;

                double checkAcc = Double.parseDouble(topUpShow.getText().toString());
                double checkCurAcc = Double.parseDouble(currencyAccount.getText().toString());
                double checkValue = Double.parseDouble(entValue.getText().toString());
                double checkRate = Double.parseDouble(entCurrencyRate.getText().toString());
                double checkBought = checkRate * checkValue;
                if(checkConditions()) {
                    if (checkBought <= checkAcc) {
                        reference.child(Long.toString(count1)).child("kwota").setValue(checkValue);
                        reference.child(Long.toString(count1)).child("nazwa").setValue(entCurrencyName.getText().toString());
                        reference.child(Long.toString(count1)).child("kurs").setValue(checkRate);
                        entValue.setText("");
                        entCurrencyName.setText("");
                        entCurrencyRate.setText("");

                        String updateacc = String.valueOf(df2.format((checkAcc - checkBought)));
                        String updateCurrencyAcc = String.valueOf(df2.format(checkCurAcc + checkBought));
                        reference1.child("investAcc").setValue(updateacc);
                        reference1.child("currencyWallet").setValue(updateCurrencyAcc);
                    } else {
                        Toast.makeText(Investments.this, "Za mało środków na konice", Toast.LENGTH_LONG).show();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        updateAccount();
    }
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private void jsonRates() {

        curRat = (TextView) findViewById(R.id.curRate);
        ratesSpinner = (Spinner) findViewById(R.id.rateSpinner);

        ArrayList<String> spinRdata = new ArrayList<>( Arrays.asList("CAD","HKD","ISK","PHP","DKK","HUF","CZK","AUD","RON",
                "SEK","IDR","INR","BRL","RUB","HRK","JPY","THB","CHF","SGD","PLN","BGN","TRY","CNY","NOK","NZD","ZAR","USD","MXN","ILS","GBP","KRW","MYR"));
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


