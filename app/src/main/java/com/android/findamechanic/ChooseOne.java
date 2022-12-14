package com.android.findamechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseOne extends AppCompatActivity {

    Button serviceProvider, customer;
    Intent intent;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_one);



        intent = getIntent();
        type = intent.getStringExtra("Home").toString().trim();

        serviceProvider = (Button)findViewById(R.id.chooseServiceProvider);
        customer = (Button)findViewById(R.id.chooseCustomer);


        serviceProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("Email")){
                    Intent loginemail  = new Intent(ChooseOne.this,ServiceEmailLogin.class);
                    startActivity(loginemail);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphone  = new Intent(ChooseOne.this,ServicePhoneLogin.class);
                    startActivity(loginphone);
                    finish();
                }
                if(type.equals("Register")){
                    Intent Register  = new Intent(ChooseOne.this,ServiceRegister.class);
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