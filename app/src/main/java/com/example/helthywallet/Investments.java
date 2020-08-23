package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Investments extends AppCompatActivity {

    TextView curRat;
    private RequestQueue mQueue;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDatalist;
    Spinner ratesSpinner;
    ImageView menu_btn;
    RecyclerView mRecyclerview;
    Adapter1 myAdapter;
    Button buyCurrency;
    EditText entValue, entCurrencyName, entCurrencyRate;

    DatabaseReference reference;

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

        mRecyclerview = findViewById(R.id.recyclerCurr);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new Adapter1(this, getMyList());
        mRecyclerview.setAdapter(myAdapter);

        buyCurrency = (Button) findViewById(R.id.buyCurrencyBtn);
        buyCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyCurrency();
            }
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
    private void readFromBase(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("currencytrasactions");

        ArrayList<Model> models = new ArrayList<>();
        Model m = new Model();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot children: dataSnapshot.getChildren()){

//                    m.setName(children.child("nazwa").getValue().toString());
//                    m.setRate(Double.parseDouble(children.child("kurs").getValue().toString()));
//                    m.setCurrencyValue(Double.parseDouble(children.child("kwota").getValue().toString()));
//                    m.setCurrencyWorthBefore(500.00); //policzone (kurs * kwota)
//                    m.setCurrencyWorthNow(600.00); //z jsona
//                    m.setProfit(100.00); //policzony
//                    m.setImg(R.drawable.transaction_icon);
//                    models.add(m);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    private ArrayList<Model> getMyList(){


        ArrayList<Model> models = new ArrayList<>();
        Model m = new Model();
        m.setName("USD"); //z bazy
        m.setCurrencyValue(100.00); //z bazy
        m.setCurrencyWorthBefore(500.00); //policzone (kurs * kwota)
        m.setCurrencyWorthNow(600.00); //z jsona
        m.setProfit(100.00); //policzony
        m.setRate(3.7890); //z bazy
        m.setImg(R.drawable.transaction_icon);
        models.add(m);

        m = new Model();
        m.setCurrencyValue(100.00);
        m.setCurrencyWorthBefore(500.00);
        m.setCurrencyWorthNow(600.00);
        m.setProfit(100.00);
        m.setRate(3.7890);
        m.setImg(R.drawable.transaction_icon);
        models.add(m);

        m = new Model();
        m.setCurrencyValue(100.00);
        m.setCurrencyWorthBefore(500.00);
        m.setCurrencyWorthNow(600.00);
        m.setProfit(100.00);
        m.setRate(3.7890);
        m.setImg(R.drawable.transaction_icon);
        models.add(m);


        return models;
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
