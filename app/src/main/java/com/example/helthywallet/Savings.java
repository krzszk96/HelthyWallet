package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Savings extends AppCompatActivity {

    EditText inTitle, inAmount, inTime, inInterest,chargeAccountVal;
    DatabaseReference reference,reference1;
    Button addDeposit,chargeBtn,withdrawBtn;
    Animation scaleUp,scaleDown;
    ImageView menu_btn;
    TextView topUpShow;

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
                    recreate();
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
        //seeData2 = (TextView) findViewById(R.id.showDeposits);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String display1 = "";

                for (DataSnapshot children: dataSnapshot.getChildren()){

                        String title = children.child("tytul").getValue().toString();
                        String amount = children.child("kwota").getValue().toString();
                        String date = children.child("okres").getValue().toString();
                        String interest = children.child("odsetki").getValue().toString();
                        String buff = title + ":   (" +  amount + ")  /  " + date + " mies /  " + interest + " %";
                        //display1 = display1 + buff;

                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.deposLayout);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT ) ;
                    layoutParams.setMargins( 0 , 0 , 0 , 20 ) ;
                    TextView text = new TextView(Savings.this);
                    text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    text.setText(buff);
                    text.setBackgroundResource(R.drawable.chart_background);
                    text.setPadding(70, 20, 0, 20);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                    text.setTextColor(Color.parseColor("#494444"));
                    linearLayout.addView(text, layoutParams);

                }
                //seeData2.setText(display1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
