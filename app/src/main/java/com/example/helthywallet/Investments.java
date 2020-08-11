package com.example.helthywallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

public class Investments extends AppCompatActivity {

    private RequestQueue mQueue;
    TextView curTxt, curRat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investments);

        mQueue = Volley.newRequestQueue(this);
        jsonRates();

    }
    private void jsonRates() {

        curTxt = (TextView) findViewById(R.id.curText);
        curRat = (TextView) findViewById(R.id.curRate);
        //String url = "https://api.nbp.pl/api/exchangerates/rates/c/usd/2016-04-04/?format=json";
        String url = "https://api.nbp.pl/api/exchangerates/tables/a/?format=json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    //JSONArray jsonArray = response.getJSONArray();
                    JSONObject test = response.optJSONObject(" ");
                    JSONArray jsonArray = response.getJSONArray("rates");
                    JSONObject rate = jsonArray.getJSONObject(0);
                    //String code = jsonArray2.getString(2);
                    //double myRate = rate.getDouble("mid");
                    //curTxt.setText("code" + response.toString());
                    curTxt.setText("code" + rate);
                    //curRat.setText(Double.toString(myRate));
                    Log.d("TEST", "TEST1" );

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
}
