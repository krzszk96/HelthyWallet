package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import java.security.spec.ECField;
import java.text.DecimalFormat;
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
                    String valuefromdatabase = dataSnapshot.child("investAcc").getValue().toString();
                    topUpShow.setText(valuefromdatabase);
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
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    private boolean checkConditions(){
        String title = inTitle.getText().toString();
        String amount = inAmount.getText().toString();
        String time = inTime.getText().toString();
        String interest = inInterest.getText().toString();

        if(title.equals("")){Toast.makeText(Savings.this, "Wpisz tytuł lokaty", Toast.LENGTH_LONG).show(); return false;}
        if(amount.equals("")){Toast.makeText(Savings.this, "Wpisz kwotę lokaty", Toast.LENGTH_LONG).show(); return false;}
        if(time.equals("")){Toast.makeText(Savings.this, "Wpisz czas trwania lokaty", Toast.LENGTH_LONG).show(); return false;}
        if(interest.equals("")){Toast.makeText(Savings.this, "Wpisz odsetki lokaty", Toast.LENGTH_LONG).show(); return false;}

        if(title.length()>15){Toast.makeText(Savings.this, "Tytuł jest za długi, maksymalnie 15 znaków", Toast.LENGTH_LONG).show(); return false;}
        if(!isNumeric(amount)){Toast.makeText(Savings.this, "Wpisana kwota nie jest liczbą", Toast.LENGTH_LONG).show(); return false;}
        if(!isNumeric(time)){Toast.makeText(Savings.this, "Wpisana kwota nie jest liczbą", Toast.LENGTH_LONG).show(); return false;}
        if(Integer.parseInt(time)>36524){Toast.makeText(Savings.this, "Wpisana liczba to więcej niż 100 lat", Toast.LENGTH_LONG).show(); return false;}
        if(!isNumeric(interest)){Toast.makeText(Savings.this, "Wpisane odsetki nie są liczbą, wpisz 4 dla 0.04%", Toast.LENGTH_LONG).show(); return false;}

        return true;
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

                int count1=0;
                for (DataSnapshot children : dataSnapshot.getChildren()){
                    count1 =  Integer.parseInt(children.getKey());
                }
                count1++;

                double checkAcc = Double.parseDouble(topUpShow.getText().toString());
                double checkValue = Double.parseDouble(inAmount.getText().toString());
                if(checkConditions()) {
                    if (checkValue <= checkAcc) {
                        reference.child(Long.toString(count1)).child("tytul").setValue(inTitle.getText().toString());
                        reference.child(Long.toString(count1)).child("kwota").setValue(inAmount.getText().toString());
                        reference.child(Long.toString(count1)).child("okres").setValue(inTime.getText().toString());
                        reference.child(Long.toString(count1)).child("odsetki").setValue(inInterest.getText().toString());
                        inTitle.setText("");
                        inAmount.setText("");
                        inTime.setText("");
                        inInterest.setText("");
                        String updateacc = String.valueOf(df2.format((checkAcc - checkValue)));
                        reference1.child("investAcc").setValue(updateacc);
                    } else {
                        Toast.makeText(Savings.this, "Za mało środków na konice", Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        updateAccount();
    }
    public void removeItem(final int position){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("deposits");
        topUpShow = (TextView) findViewById(R.id.accBalance);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String checkId = modelsList.get(position).getId();

                double baseAmount = Double.parseDouble(dataSnapshot.child(checkId).child("kwota").getValue().toString());
                double accValue = Double.parseDouble(topUpShow.getText().toString());
                double update = accValue + baseAmount;
                reference1.child("investAcc").setValue(String.valueOf(df2.format(update)));
                updateAccount();

                reference.child(checkId).removeValue();
                modelsList.remove(position);
                recyclerAdapterDeposits.notifyItemRemoved(position);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void endItem(final int position){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("deposits");
        topUpShow = (TextView) findViewById(R.id.accBalance);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String checkId = modelsList.get(position).getId();

                //kwota * mies na dni * oprocentowanie 0.0x / 365

                double baseAmount = Double.parseDouble(dataSnapshot.child(checkId).child("kwota").getValue().toString());
                double timePeriod = Double.parseDouble(dataSnapshot.child(checkId).child("okres").getValue().toString());
                double interest = Double.parseDouble(dataSnapshot.child(checkId).child("odsetki").getValue().toString());

                double calculateProfitGross = (baseAmount * timePeriod * (interest/100)) / 365; //calculate total profit without tax
                double calculateTax = calculateProfitGross * 0.19;
                double calculateProfitNet = calculateProfitGross - calculateTax;

                Log.d("TAG", "gross:" + calculateProfitGross);
                Log.d("TAG1", "tax:" + calculateTax);
                Log.d("TAG2", "net:" + calculateProfitNet);

                double accValue = Double.parseDouble(topUpShow.getText().toString());
                double update = accValue + baseAmount + calculateProfitNet; //calculate profit with tax
                reference1.child("investAcc").setValue(String.valueOf(df2.format(update)));
                updateAccount();

                reference.child(checkId).removeValue();
                modelsList.remove(position);
                recyclerAdapterDeposits.notifyItemRemoved(position);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    public void displayData(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("deposits");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    ClearAll();
                    for (DataSnapshot children: dataSnapshot.getChildren()){

                            String title = children.child("tytul").getValue().toString();
                            String amount = children.child("kwota").getValue().toString();
                            String date = children.child("okres").getValue().toString();
                            String interest = children.child("odsetki").getValue().toString();
                            String id = children.getKey();

                            //add model
                            DepositModel deposit = new DepositModel();
                            deposit.setTitle(title);
                            deposit.setAmount(amount);
                            deposit.setTime(date);
                            deposit.setInterest(interest);
                            deposit.setImg(R.drawable.bank_icon);
                            deposit.setId(id);
                            modelsList.add(deposit);

                    }
                }catch (Exception e){}
                recyclerAdapterDeposits = new RecyclerAdapterDeposits(getApplicationContext(), modelsList);
                recyclerView.setAdapter(recyclerAdapterDeposits);
                recyclerAdapterDeposits.notifyDataSetChanged();

                recyclerAdapterDeposits.setOnItemClickListener(new RecyclerAdapterDeposits.OnItemClickListener() {
                    @Override
                    public void onDeleteClick(final int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Savings.this);
                        builder.setTitle("Usuwanie lokaty");
                        builder.setMessage("Wybierz czy chcesz zakończyć lokatę (obliczenie zysku), czy usunąć lokatę z portfela");

                        builder.setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeItem(position);
                            }
                        });
                        builder.setNegativeButton("Zakończ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                endItem(position);
                            }
                        });
                        builder.create().show();
                    }
                });
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
