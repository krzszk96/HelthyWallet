package com.example.helthywallet;

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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

public class MainViewChart extends AppCompatActivity {

    EditText inCategory, inAmount, inTitle, inDate;
    TextView seeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_chart);

        inCategory = (EditText) findViewById(R.id.inCategory);
        inAmount = (EditText) findViewById(R.id.inAmount);
        inTitle = (EditText) findViewById(R.id.inTitle);
        inDate = (EditText) findViewById(R.id.inDate);

        seeData = (TextView) findViewById(R.id.viewData);



    }
}
