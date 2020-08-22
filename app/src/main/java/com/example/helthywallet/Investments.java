package com.example.helthywallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Investments extends AppCompatActivity {

    TextView curTxt, curRat;
    private RequestQueue mQueue;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDatalist;
    Spinner ratesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investments);

        mQueue = Volley.newRequestQueue(this);
        jsonRates();

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
