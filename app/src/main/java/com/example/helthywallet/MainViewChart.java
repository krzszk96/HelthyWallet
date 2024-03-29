package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class MainViewChart extends AppCompatActivity {

    EditText inAmount, inTitle, newCat, inAmountV, inDateD, inDateM, inDateY;
    Button addCategory, addData;
    Spinner spinCats, iconSpinner;
    ImageView menu_btn;

    DatabaseReference reference, refSpin, refCat, referenceIcon;
    ValueEventListener listener;
    ArrayAdapter<String> adapter;

    int clickedPosition;

    ArrayList<String> spinnerDatalist;

    Animation scaleUp,scaleDown;

    RecyclerView recyclerView; //widget

    private RecyclerAdapterTransactions recyclerAdapterTransactions; //firebase

    private ArrayList<TransactionModel> modelsList; //variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_chart);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        menu_btn = (ImageView) findViewById(R.id.image_menu);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_btn.startAnimation(scaleUp);
                menu_btn.startAnimation(scaleDown);
                startActivity(new Intent(MainViewChart.this, MainScreen.class));
            }
        });

        inAmountV = (EditText) findViewById(R.id.addAmountV);
        inAmountV.setText("-");
        inAmount = (EditText) findViewById(R.id.addAmount);
        inTitle = (EditText) findViewById(R.id.addTitle);
        inDateD = (EditText) findViewById(R.id.addDateD);
        inDateM = (EditText) findViewById(R.id.addDateM);
        inDateY = (EditText) findViewById(R.id.addDateY);

        recyclerView = findViewById(R.id.recyclerTransactions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        displayData2();

        addCategory = (Button) findViewById(R.id.addCatBtn);
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory.startAnimation(scaleUp);
                addCategory.startAnimation(scaleDown);
                addCategory();
            }
        });

        spinCats = (Spinner) findViewById(R.id.categorySpinner);
        spinnerDatalist = new ArrayList<>();
        adapter = new ArrayAdapter<String>(MainViewChart.this, android.R.layout.simple_spinner_dropdown_item, spinnerDatalist);
        spinCats.setAdapter(adapter);
        retrieveData();

        iconSpinner = findViewById(R.id.iconSpinner);

        SimpleImageArrayAdapter adapter = new SimpleImageArrayAdapter(getApplicationContext(),
                new Integer[]{R.drawable.cat_bill_icon, R.drawable.cat_car_icon,
                        R.drawable.cat_child_icon, R.drawable.cat_family_icon,
                        R.drawable.cat_food_icon , R.drawable.cat_home_icon ,
                        R.drawable.cat_present_icon , R.drawable.cat_shopping_icon,
                        R.drawable.cat_work_icon});
        iconSpinner.setAdapter(adapter);

        iconSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clickedPosition = parent.getSelectedItemPosition();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addData = (Button) findViewById(R.id.addDataBtn);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData.startAnimation(scaleUp);
                addData.startAnimation(scaleDown);
                addTransaction();
            }
        });
    }
    public boolean checkConditions(){
        String title = inTitle.getText().toString();
        String sign = inAmountV.getText().toString();
        String amount = inAmount.getText().toString();
        String textD = inDateD.getText().toString();
        String textM = inDateM.getText().toString();
        String textY = inDateY.getText().toString();

        if(title.equals("")){Toast.makeText(MainViewChart.this,
                "Wpisz tytuł", Toast.LENGTH_SHORT).show(); return false;}
        if(sign.equals("")){Toast.makeText(MainViewChart.this,
                "Wpisz znak + lub -", Toast.LENGTH_SHORT).show(); return false;}
        if(amount.equals("")){Toast.makeText(MainViewChart.this,
                "Wpisz kwotę transakcji", Toast.LENGTH_SHORT).show(); return false;}
        if(textD.equals("")||textM.equals("")||textY.equals("")){
            Toast.makeText(MainViewChart.this, "Wpisz pełną datę",
                    Toast.LENGTH_SHORT).show(); return false;}

        int day = Integer.valueOf(textD);
        int month = Integer.valueOf(textM);
        int year = Integer.valueOf(textY);

        if(title.length()>=15){Toast.makeText(MainViewChart.this,
                "Tytuł za długi, maks 15 znaków", Toast.LENGTH_SHORT).show(); return false;}
        if(sign.length()>1){Toast.makeText(MainViewChart.this,
                "można wpisać tylko + lub -", Toast.LENGTH_SHORT).show(); return false;}
        if(!isNumeric(amount)){Toast.makeText(MainViewChart.this,
                "Uwaga tekst zamiast kwoty!", Toast.LENGTH_SHORT).show(); return false;}
        if(day > 31 || day < 0 ){Toast.makeText(MainViewChart.this,
                "Błędny dzień", Toast.LENGTH_SHORT).show(); return false;}
        if(month > 12 || month < 0 ) {Toast.makeText(MainViewChart.this,
                "Błędny miesiąc", Toast.LENGTH_SHORT).show(); return false;}
        if(year > 2500 || year < 2019 ) {Toast.makeText(MainViewChart.this,
                "Błędny rok", Toast.LENGTH_SHORT).show(); return false;}

        return true;
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
    public void addCategory(){

        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        refSpin = FirebaseDatabase.getInstance().getReference("users").child(ref);

        newCat = (EditText) findViewById(R.id.addCategoryBase);

        refSpin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String newCategory = newCat.getText().toString();
                String clickedIcon = String.valueOf(clickedPosition);
                refSpin.child("categories").child(newCategory).setValue("0");
                refSpin.child("categories").child("catIcon").child(newCategory).setValue(clickedIcon);
                newCat.setText("");
                spinnerDatalist.clear();
                retrieveData();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void retrieveData(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("categories");
        listener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spinnerDatalist.clear();
                for(DataSnapshot item:dataSnapshot.getChildren()){
                    String category = item.getKey();
                    if(!category.equals("catIcon")){
                        spinnerDatalist.add(category);
                    }

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public void addTransaction(){

        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String cat = spinCats.getSelectedItem().toString();
        refCat = FirebaseDatabase.getInstance().getReference("users").child(ref).child("categories").child(cat);

        refCat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count1=0;
                for (DataSnapshot children : dataSnapshot.getChildren()){
                        count1 =  Integer.parseInt(children.getKey());
                }
                count1++;
                String amount = inAmountV.getText().toString() + inAmount.getText().toString();


                if(checkConditions()) {
                    String date = inDateD.getText().toString() + "/" + inDateM.getText().toString() + "/" + inDateY.getText().toString();
                    refCat.child(Long.toString(count1)).child("data").setValue(date);
                    refCat.child(Long.toString(count1)).child("tytul").setValue(inTitle.getText().toString());
                    refCat.child(Long.toString(count1)).child("kwota").setValue(amount);
                    inTitle.setText("");
                    inAmount.setText("");
                    inDateD.setText(""); inDateM.setText(""); inDateY.setText("");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        spinnerDatalist.clear();
        retrieveData();
        adapter.notifyDataSetChanged();
    }
    public void removeItem(final int position){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance()
                .getReference("users").child(ref).child("categories");

        String category = modelsList.get(position).getCategory();
        String checkId = modelsList.get(position).getId();

        reference.child(category).child(checkId).removeValue();

        modelsList.remove(position);
        recyclerAdapterTransactions.notifyItemRemoved(position);
    }
    public void displayData2(){
        String ref = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(ref).child("categories");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    ClearAll();
                    int[] imageArray = {R.drawable.cat_bill_icon, R.drawable.cat_car_icon,
                            R.drawable.cat_child_icon, R.drawable.cat_family_icon,
                            R.drawable.cat_food_icon , R.drawable.cat_home_icon ,
                            R.drawable.cat_present_icon , R.drawable.cat_shopping_icon,
                            R.drawable.cat_work_icon};

                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        for (DataSnapshot i : children.getChildren()) {

                            final String category = children.getKey();

                            if(!category.equals("catIcon")) {
                                String id = i.getKey();
                                String title = i.child("tytul").getValue().toString();
                                String amount = i.child("kwota").getValue().toString();
                                String date = i.child("data").getValue().toString();
                                String img = dataSnapshot.child("catIcon").child(category).getValue().toString();
                                int imgNumber = Integer.parseInt(img);

                                TransactionModel transaction = new TransactionModel();
                                transaction.setCategory(category);
                                transaction.setTitle(title);
                                transaction.setAmount(amount);
                                transaction.setDate(date);
                                transaction.setImg(imageArray[imgNumber]);
                                transaction.setId(id);
                                modelsList.add(transaction);
                                Log.d("TAG", "msg" + img);
                            }
                        }
                    }
                }catch (Exception e){}
                recyclerAdapterTransactions = new RecyclerAdapterTransactions(getApplicationContext(), modelsList);
                recyclerView.setAdapter(recyclerAdapterTransactions);
                recyclerAdapterTransactions.notifyDataSetChanged();

                recyclerAdapterTransactions.setOnItemClickListener(new RecyclerAdapterTransactions.OnItemClickListener() {
                    @Override
                    public void onDeleteClick(final int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainViewChart.this);
                        builder.setTitle("Usuwanie transakcji");
                        builder.setMessage("Czy na pewno chcesz usunąć wybraną transakcję ?");

                        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeItem(position);
                            }
                        });
                        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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

            if(recyclerAdapterTransactions != null){
                recyclerAdapterTransactions.notifyDataSetChanged();
            }
        }
        modelsList = new ArrayList<>();
    }
}
