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

    TextView inc, exp, dep, cur;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        displayIncExp();
    }
    public void displayIncExp(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid(); /// not used??
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("categories");
        inc = (TextView) findViewById(R.id.income);
        exp = (TextView) findViewById(R.id.expenses);
        dep = (TextView) findViewById(R.id.deposit);
        cur = (TextView) findViewById(R.id.curencies);

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
                String dispI = "+" + income + " PLN";
                String dispE = "-" + expense + " PLN";
                inc.setText(dispI);
                exp.setText(dispE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
