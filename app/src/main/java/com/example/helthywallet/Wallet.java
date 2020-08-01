package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Wallet extends AppCompatActivity {

    TextView inc, exp, dep, cur, balance;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        displayIncExp();
        displayDep();
    }
    public void countWallet(double income, double expense){
        balance = (TextView) findViewById(R.id.accBalance);
        double accBalance=0;

        accBalance = income - expense;
        String displayBal = Double.toString(accBalance);

        if(income<=expense){
            displayBal = "-" + displayBal + " PLN";
        }else{
            displayBal = "+" + displayBal + " PLN";
        }
        balance.setText(displayBal);
    }
    public void displayIncExp(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("categories");
        inc = (TextView) findViewById(R.id.income);
        exp = (TextView) findViewById(R.id.expenses);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                double income = 0;
                double expense = 0;

                for (DataSnapshot children: dataSnapshot.getChildren()){

                    long count= children.getChildrenCount();
                    for (int i=0; i< count; i++){

                        String amount = children.child(Integer.toString(i)).child("kwota").getValue().toString();
                        char ch1 = amount.charAt(0);

                        if(ch1 == '+'){
                            income = income + Double.parseDouble(amount.substring(1));
                        }else {
                            expense = expense + Double.parseDouble(amount.substring(1));
                        }
                    }
                }
                countWallet(income,expense);
                String dispI = "+" + income + " PLN";
                String dispE = "-" + expense + " PLN";
                inc.setText(dispI);
                exp.setText(dispE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public void displayDep(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("deposits");
        dep = (TextView) findViewById(R.id.deposit);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                double deposits = 0;

                for (DataSnapshot children: dataSnapshot.getChildren()){

                        String amount = children.child("kwota").getValue().toString();
                        deposits = deposits + Double.parseDouble(amount);
                }
                String disp = "+" + deposits + " PLN";
                dep.setText(disp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
