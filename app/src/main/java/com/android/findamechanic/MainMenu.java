package com.android.findamechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    Button signInEmail, signInPhone, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        signInEmail = findViewById(R.id.signInEmail);
        signInPhone = findViewById(R.id.signInPhone);
        register = findViewById(R.id.register);

        signInEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signEmail = new Intent(MainMenu.this, ChooseOne.class);
                signEmail.putExtra("Home", "Email");
                startActivity(signEmail);
                finish();
            }
        });

        signInPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signPhone = new Intent(MainMenu.this, ChooseOne.class);
                signPhone.putExtra("Home", "Phone");
                startActivity(signPhone);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(MainMenu.this, ChooseOne.class);
                reg.putExtra("Home", "Register");
                startActivity(reg);
                finish();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}