package com.example.helthywallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainScreen extends AppCompatActivity {

    private Button wallet_btn, manage_btn, savings_btn, reports_btn, invest_btn, settings_btn, logout_btn;
    Animation scaleUp,scaleDown;

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

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        manage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manage_btn.startAnimation(scaleUp);
                manage_btn.startAnimation(scaleDown);
                startActivity(new Intent(MainScreen.this, MainViewChart.class));
            }
        });
        wallet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallet_btn.startAnimation(scaleUp);
                wallet_btn.startAnimation(scaleDown);
                startActivity(new Intent(MainScreen.this, Wallet.class));
            }
        });
        savings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savings_btn.startAnimation(scaleUp);
                savings_btn.startAnimation(scaleDown);
                startActivity(new Intent(MainScreen.this, Savings.class));
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_btn.startAnimation(scaleUp);
                logout_btn.startAnimation(scaleDown);
                startActivity(new Intent(MainScreen.this, MainActivity.class));
            }
        });
        invest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_btn.startAnimation(scaleUp);
                logout_btn.startAnimation(scaleDown);
                startActivity(new Intent(MainScreen.this, Investments.class));
            }
        });
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings_btn.startAnimation(scaleUp);
                settings_btn.startAnimation(scaleDown);
            }
        });
    }
}
