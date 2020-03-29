package com.example.helthywallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    EditText inCategory, inAmount;
    TextView seeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_chart);

        inCategory = (EditText) findViewById(R.id.inCategory);
        inAmount = (EditText) findViewById(R.id.inAmount);

        seeData = (TextView) findViewById(R.id.viewData);

        try {
            Database db = new Database(this);
            db.open();
            seeData.setText(db.getData());
            db.close();
        }
        catch (SQLException e){
            Toast.makeText(MainViewChart.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void btnSubmit(View v){
        String category = inCategory.getText().toString().trim();
        String amount = inAmount.getText().toString().trim();

        try{

            Database db = new Database(this);
            db.open();
            db.createEntry(category, amount);
            db.close();
            Toast.makeText(MainViewChart.this, "Succes save", Toast.LENGTH_SHORT).show();
            inCategory.setText("");
            inAmount.setText("");
            finish();
            startActivity(getIntent());

        } catch (SQLException e) {
            Toast.makeText(MainViewChart.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    public void btnShowData(View v){

        //startActivity(new Intent(this, Data.class));

    }
    public void btnEditData(View v){

        try{
            Database db = new Database(this);
            db.open();
            db.updateEntry("1", "pensja", " 5000");
            db.close();
            Toast.makeText(MainViewChart.this, "Success update", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        }
        catch (SQLException e){
            Toast.makeText(MainViewChart.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void btnDeleteData(View v){

        try{
            Database db = new Database(this);
            db.open();
            db.deleteEntry("1");
            db.close();
            Toast.makeText(MainViewChart.this, "Success delete!!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        }
        catch (SQLException e){
            Toast.makeText(MainViewChart.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
