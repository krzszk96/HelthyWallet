package com.example.helthywallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {

    Animation scaleUp,scaleDown;
    ImageView menu_btn;
    Button changeEmail, changePassword;
    private FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        fUser = FirebaseAuth.getInstance().getCurrentUser();


        menu_btn = (ImageView) findViewById(R.id.image_menu);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_btn.startAnimation(scaleUp);
                menu_btn.startAnimation(scaleDown);
                startActivity(new Intent(Settings.this, MainScreen.class));
            }
        });

        changeEmail = (Button) findViewById(R.id.changeEmailBtn);
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmail.startAnimation(scaleUp);
                changeEmail.startAnimation(scaleDown);
            }
        });

        changePassword = (Button) findViewById(R.id.changePasswordBtn);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword.startAnimation(scaleUp);
                changePassword.startAnimation(scaleDown);
            }
        });

    }
}
