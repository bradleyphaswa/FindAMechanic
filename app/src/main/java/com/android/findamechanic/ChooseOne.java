package com.android.findamechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseOne extends AppCompatActivity {

    Button mechanic, spareDealer, carDealer, customer;
    Intent intent;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_one);



        intent = getIntent();
        type = intent.getStringExtra("Home").toString().trim();

        mechanic = findViewById(R.id.chooseMechanic);
        spareDealer = (Button)findViewById(R.id.chooseSpareDealer);
        carDealer = findViewById(R.id.chooseCarDealer);
        customer = (Button)findViewById(R.id.chooseCustomer);

        mechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("Email")){
                    Intent loginemail  = new Intent(ChooseOne.this, MechanicEmailLogin.class);
                    startActivity(loginemail);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphone  = new Intent(ChooseOne.this, MechanicPhoneLogin.class);
                    startActivity(loginphone);
                    finish();
                }
                if(type.equals("Register")){
                    Intent Register  = new Intent(ChooseOne.this, MechanicRegister.class);
                    startActivity(Register);
                    finish();
                }
            }
        });

        spareDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("Email")){
                    Intent loginemail  = new Intent(ChooseOne.this, SpareEmailLogin.class);
                    startActivity(loginemail);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphone  = new Intent(ChooseOne.this, SparePhoneLogin.class);
                    startActivity(loginphone);
                    finish();
                }
                if(type.equals("Register")){
                    Intent Register  = new Intent(ChooseOne.this, SpareRegister.class);
                    startActivity(Register);
                    finish();
                }
            }
        });

        carDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("Email")){
                    Intent loginemail  = new Intent(ChooseOne.this, CarEmailLogin.class);
                    startActivity(loginemail);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphone  = new Intent(ChooseOne.this, CarPhoneLogin.class);
                    startActivity(loginphone);
                    finish();
                }
                if(type.equals("Register")){
                    Intent Register  = new Intent(ChooseOne.this, CarRegister.class);
                    startActivity(Register);
                    finish();
                }
            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("Email")){
                    Intent loginemailcust  = new Intent(ChooseOne.this,CustomerEmailLogin.class);
                    startActivity(loginemailcust);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphonecust  = new Intent(ChooseOne.this, CustomerPhoneLogin.class);
                    startActivity(loginphonecust);
                    finish();
                }
                if(type.equals("Register")){
                    Intent Registercust  = new Intent(ChooseOne.this,CustomerRegister.class);
                    startActivity(Registercust);
                }

            }
        });
    }
}