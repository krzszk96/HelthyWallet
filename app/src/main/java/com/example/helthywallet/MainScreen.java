package com.example.helthywallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreen extends AppCompatActivity {

    private Button wallet_btn, manage_btn, savings_btn, reports_btn, invest_btn, settings_btn, logout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        wallet_btn = (Button) findViewById(R.id.nav_wallet);
        manage_btn = (Button) findViewById(R.id.nav_manage);
        savings_btn = (Button) findViewById(R.id.nav_savings);
        reports_btn = (Button) findViewById(R.id.nav_reports);
        invest_btn = (Button) findViewById(R.id.nav_invest);
        settings_btn = (Button) findViewById(R.id.nav_settings);
        logout_btn = (Button) findViewById(R.id.nav_logout);

        manage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, MainViewChart.class));
            }
        });
        wallet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, Wallet.class));
            }
        });
        savings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, Savings.class));
            }
        });
    }
}
