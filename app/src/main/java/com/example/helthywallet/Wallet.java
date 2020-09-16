package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Wallet extends AppCompatActivity {

    TextView dep, cur, balance, showPerc, topUpShow;
    ImageView menu_btn;
    DatabaseReference reference,reference1,reference2;
    DatabaseReference newReference;
    Animation scaleUp,scaleDown;

    //widget
    RecyclerView recyclerView;

    //firebase
    private RecyclerAdapterWallet recyclerAdapterWallet;

    //variables
    private ArrayList<WalletModel> modelsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        menu_btn = (ImageView) findViewById(R.id.image_menu);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_btn.startAnimation(scaleUp);
                menu_btn.startAnimation(scaleDown);
                startActivity(new Intent(Wallet.this, MainScreen.class));
            }
        });

        recyclerView = findViewById(R.id.recyclerCat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        displayIncExp();
        displayDep();
        displayCurrency();
        updateWallet();
        updateAccount();

    }
    public void updateWallet(){
        balance = (TextView) findViewById(R.id.accBalance);

        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        newReference = FirebaseDatabase.getInstance().getReference("users").child(ref);

        newReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newReference.child("wallet").setValue(balance.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void updateAccount(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference1 = FirebaseDatabase.getInstance().getReference("users").child(ref);

        topUpShow = (TextView) findViewById(R.id.accBalanceInvest);

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("investAcc")) {
                    String valueformdatabase = dataSnapshot.child("investAcc").getValue().toString();
                    topUpShow.setText(valueformdatabase);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public void countWallet(double income, double expense){
        balance = (TextView) findViewById(R.id.accBalance);
        double accBalance=0;

        accBalance = income - expense;
        String displayBal = Double.toString(accBalance);

        if(income>=expense){displayBal = "+" + displayBal;}
        balance.setText(displayBal);
    }
    public void displayIncExp(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("categories");
        reference2 = FirebaseDatabase.getInstance().getReference("users").child(ref);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                double income = 0;
                double expense = 0;
                
                for (DataSnapshot children: dataSnapshot.getChildren()){
                        for (DataSnapshot child : children.getChildren()) {
                           String category = children.getKey();

                            if(!category.equals("catIcon")) {
                                String amount = child.child("kwota").getValue().toString();
                                char ch1 = amount.charAt(0);

                                if (ch1 == '+') {
                                    income = income + Double.parseDouble(amount.substring(1));
                                } else {
                                    expense = expense + Double.parseDouble(amount.substring(1));
                                }
                            }
                        }
                }
                reference2.child("income").setValue(income);
                reference2.child("expenses").setValue(expense);
                countWallet(income,expense);
                drawPie(income,expense);
                displayCat(income,expense);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public void displayCat(final double income, final double expense){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("categories");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ClearAll();
                double amount = 0;
                double percent = 0;
                char ch1='-';
                int[] imageArray = {R.drawable.cat_bill_icon, R.drawable.cat_car_icon,
                        R.drawable.cat_child_icon, R.drawable.cat_family_icon,
                        R.drawable.cat_food_icon , R.drawable.cat_home_icon,
                        R.drawable.cat_present_icon , R.drawable.cat_shopping_icon,
                        R.drawable.cat_work_icon};

                for (DataSnapshot children : dataSnapshot.getChildren()) {

                    String category = children.getKey();
                    if(!category.equals("catIcon")) {

                        for (DataSnapshot child1 : children.getChildren()) {

                            String amountTemp = child1.child("kwota").getValue().toString();
                            ch1 = amountTemp.charAt(0);
                            amount = amount + Double.parseDouble(amountTemp.substring(1));
                        }
                        if (ch1 == '+') {
                            percent = Math.round(amount * 100 / income);
                        }
                        if (ch1 == '-') {
                            percent = Math.round(amount * 100 / expense);
                        }
                        String img = dataSnapshot.child("catIcon").child(category).getValue().toString();
                        int imgNumber = Integer.parseInt(img);

                        //add model
                        String displayAmount = ch1 + String.valueOf(amount);
                        WalletModel walletModel = new WalletModel();
                        walletModel.setCategory(category);
                        walletModel.setAmount(displayAmount);
                        walletModel.setPercent(percent);
                        walletModel.setImg(imageArray[imgNumber]);
                        modelsList.add(walletModel);
                        amount = 0;
                    }
                }
                recyclerAdapterWallet = new RecyclerAdapterWallet(getApplicationContext(), modelsList);
                recyclerView.setAdapter(recyclerAdapterWallet);
                recyclerAdapterWallet.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    private void ClearAll(){
        if (modelsList != null){
            modelsList.clear();

            if(recyclerAdapterWallet != null){
                recyclerAdapterWallet.notifyDataSetChanged();
            }
        }
        modelsList = new ArrayList<>();
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
                dep.setText(String.valueOf(deposits));
                reference2.child("totalDeposits").setValue(deposits);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public void displayCurrency(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("currencyWallet");
        cur = (TextView) findViewById(R.id.curencies);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String wallet = dataSnapshot.getValue().toString();
                    cur.setText(wallet);
                }catch (Exception e){}

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

        config.startAngle(-90)
                .addData(new SimplePieInfo(income, Color.parseColor("#00ff00"), desc1))
                .addData(new SimplePieInfo(expense, Color.parseColor("#ff0000"), desc2))
                .drawText(true)
                .textSize(40)
                .guideLineWidth(5)
                .guideLineMarginStart(0)
                .pieRadius(350)
                .autoSize(true)
                .duration(2000);

        mAnimatedPieView.applyConfig(config);
        mAnimatedPieView.start();
    }
}
