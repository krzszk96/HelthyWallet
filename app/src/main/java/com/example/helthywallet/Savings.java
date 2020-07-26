package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Savings extends AppCompatActivity {

    EditText inTitle, inAmount, inTime, inInterest;
    DatabaseReference reference;
    Button addDeposit;
    TextView seeData2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings);

        addDeposit = (Button) findViewById(R.id.addDepoBtn);
        addDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDeposit();
            }
        });

        displayData();
    }
    public void addDeposit(){

        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("deposits");

        inTitle = (EditText) findViewById(R.id.addTitleDep);
        inAmount = (EditText) findViewById(R.id.addAmountDep);
        inTime = (EditText) findViewById(R.id.addTimeDep);
        inInterest = (EditText) findViewById(R.id.addInterestDep);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long count= dataSnapshot.getChildrenCount();
                reference.child(Long.toString(count)).child("tytul").setValue(inTitle.getText().toString());
                reference.child(Long.toString(count)).child("kwota").setValue(inAmount.getText().toString());
                reference.child(Long.toString(count)).child("okres").setValue(inTime.getText().toString());
                reference.child(Long.toString(count)).child("odsetki").setValue(inInterest.getText().toString());
                inTitle.setText("");
                inAmount.setText("");
                inTime.setText("");
                inInterest.setText("");
                recreate();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public void displayData(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("deposits");
        seeData2 = (TextView) findViewById(R.id.showDeposits);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String display1 = "";

                for (DataSnapshot children: dataSnapshot.getChildren()){

                        String title = children.child("tytul").getValue().toString();
                        String amount = children.child("kwota").getValue().toString();
                        String date = children.child("okres").getValue().toString();
                        String interest = children.child("odsetki").getValue().toString();
                        String buff = "Lok: " + title + " -A:" +  amount + " -Mies:" + date + " -%:" + interest + "\n";
                        display1 = display1 + buff;

                }
                seeData2.setText(display1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
