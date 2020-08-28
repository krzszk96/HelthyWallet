package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Savings extends AppCompatActivity {

    EditText inTitle, inAmount, inTime, inInterest,chargeAccountVal;
    DatabaseReference reference,reference1;
    Button addDeposit,chargeBtn,withdrawBtn;
    Animation scaleUp,scaleDown;
    ImageView menu_btn;
    TextView topUpShow;

    //widget
    RecyclerView recyclerView;

    //firebase
    private RecyclerAdapterDeposits recyclerAdapterDeposits;

    //variables
    private ArrayList<DepositModel> modelsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings);

        menu_btn = (ImageView) findViewById(R.id.image_menu);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Savings.this, MainScreen.class));
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

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        recyclerView = findViewById(R.id.recyclerDep);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        addDeposit = (Button) findViewById(R.id.addDepoBtn);
        addDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDeposit.startAnimation(scaleUp);
                addDeposit.startAnimation(scaleDown);
                addDeposit();
            }
        });

        displayData();
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
                }else{
                    reference1.child("investAcc").setValue(0);
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
                        Toast.makeText(Savings.this, "Za mało środków na konice", Toast.LENGTH_LONG).show();
                    }else{
                        investAccBalance = investAccBalance - operation;
                        topUpShow.setText(Double.toString(investAccBalance));
                        reference1.setValue(topUpShow.getText().toString());
                        chargeAccountVal.setText("");
                    }

                }else{
                    Toast.makeText(Savings.this, "Brak środków do wypłacenia", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public void addDeposit(){

        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("deposits");
        reference1 = FirebaseDatabase.getInstance().getReference("users").child(ref);

        inTitle = (EditText) findViewById(R.id.addTitleDep);
        inAmount = (EditText) findViewById(R.id.addAmountDep);
        inTime = (EditText) findViewById(R.id.addTimeDep);
        inInterest = (EditText) findViewById(R.id.addInterestDep);
        topUpShow = (TextView) findViewById(R.id.accBalance);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long count= dataSnapshot.getChildrenCount();
                double checkAcc = Double.parseDouble(topUpShow.getText().toString());
                double checkValue = Double.parseDouble(inAmount.getText().toString());

                if( checkValue <= checkAcc){
                    reference.child(Long.toString(count)).child("tytul").setValue(inTitle.getText().toString());
                    reference.child(Long.toString(count)).child("kwota").setValue(inAmount.getText().toString());
                    reference.child(Long.toString(count)).child("okres").setValue(inTime.getText().toString());
                    reference.child(Long.toString(count)).child("odsetki").setValue(inInterest.getText().toString());
                    inTitle.setText("");
                    inAmount.setText("");
                    inTime.setText("");
                    inInterest.setText("");
                    String updateacc = String.valueOf( checkAcc - checkValue);
                    reference1.child("investAcc").setValue(updateacc);
                    //recreate();
                }else{
                    Toast.makeText(Savings.this, "Za mało środków na konice", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public void displayData(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("deposits");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    ClearAll();

                    for (DataSnapshot children: dataSnapshot.getChildren()){

                            String title = children.child("tytul").getValue().toString();
                            String amount = children.child("kwota").getValue().toString();
                            String date = children.child("okres").getValue().toString();
                            String interest = children.child("odsetki").getValue().toString();
                            String buff = title + ":   (" +  amount + ")  /  " + date + " mies /  " + interest + " %";

                            DepositModel deposit = new DepositModel();
                            deposit.setTitle(title);
                            deposit.setAmount(amount);
                            deposit.setTime(date);
                            deposit.setInterest(interest);
                            deposit.setImg(R.drawable.bank_icon);
                            modelsList.add(deposit);

                    }
                }catch (Exception e){}
                recyclerAdapterDeposits = new RecyclerAdapterDeposits(getApplicationContext(), modelsList);
                recyclerView.setAdapter(recyclerAdapterDeposits);
                recyclerAdapterDeposits.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    private void ClearAll(){
        if (modelsList != null){
            modelsList.clear();

            if(recyclerAdapterDeposits != null){
                recyclerAdapterDeposits.notifyDataSetChanged();
            }
        }
        modelsList = new ArrayList<>();
    }
}
