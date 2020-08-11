package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

public class Wallet extends AppCompatActivity {

    TextView inc, exp, dep, cur, balance;
    ImageView menu_btn;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        menu_btn = (ImageView) findViewById(R.id.image_menu);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Wallet.this, MainScreen.class));
            }
        });

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
        //inc = (TextView) findViewById(R.id.income);
        //exp = (TextView) findViewById(R.id.expenses);

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
                drawPie(income,expense);
                //String dispI = "+" + income + " PLN";
                //String dispE = "-" + expense + " PLN";
                //inc.setText(dispI);
                //exp.setText(dispE);
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
    public void drawPie(double income, double expense){

        AnimatedPieView mAnimatedPieView = findViewById(R.id.pieView);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        String desc1 = "+" + income + " PLN";
        String desc2 = "-" + expense + " PLN";

        config.startAngle(-90)// Starting angle offset
                .addData(new SimplePieInfo(income, Color.parseColor("#00ff00"), desc1))//Data (bean that implements the IPieInfo interface)
                .addData(new SimplePieInfo(expense, Color.parseColor("#ff0000"), desc2))
                .floatExpandAngle(15f)
                .drawText(true)
                .textSize(40)
                .guideLineWidth(5)
                .guideLineMarginStart(0)
                .pieRadius(300)
                .duration(2000);// draw pie animation duration

// The following two sentences can be replace directly 'mAnimatedPieView.start (config); '
        mAnimatedPieView.applyConfig(config);
        mAnimatedPieView.start();
    }
}
