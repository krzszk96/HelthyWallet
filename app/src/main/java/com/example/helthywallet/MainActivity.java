package com.example.helthywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText entMail, entPassw;
    private Button loginBtn, singupBtn;

    private FirebaseAuth mAuth;
    private FirebaseDatabase userData;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        entMail = (EditText) findViewById(R.id.logEmail);
        entPassw = (EditText) findViewById(R.id.logPasswd);

        mAuth = FirebaseAuth.getInstance();

        singupBtn = (Button) findViewById(R.id.signUpBtn);
        singupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUpUser(entMail.getText().toString(), entPassw.getText().toString());
            }
        });

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(entMail.getText().toString(), entPassw.getText().toString());
            }
        });
    }
    public void loginUser(String email, String password){

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    entMail.setText("");
                    entPassw.setText("");
                    startActivity(new Intent(MainActivity.this, MainScreen.class));
                }else{
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void saveUserData(String email){

        userData = FirebaseDatabase.getInstance();
        reference = userData.getReference("users");

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //create new user in database
        Users helperClass = new Users(email);
        reference.child(id).setValue(helperClass);
    }
    public void singUpUser(final String email, final String password){

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    saveUserData(email);
                    Toast.makeText(MainActivity.this, "Registration Succesfull!.", Toast.LENGTH_SHORT).show();
                    entMail.setText("");
                    entPassw.setText("");
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
