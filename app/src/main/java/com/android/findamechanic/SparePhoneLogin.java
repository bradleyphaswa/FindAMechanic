package com.android.findamechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SparePhoneLogin extends AppCompatActivity {

    EditText phoneNo;
    Button sendOtp, emailLogin;
    TextView createAccount;
    String number;
    FirebaseAuth mFauth;

    public final static String SPARE_PHONE_LOGIN_NUMBER =  "PhoneNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spare_phone_login);

        phoneNo = findViewById(R.id.spareNumber);
        sendOtp = findViewById(R.id.spareOtp);
        emailLogin = findViewById(R.id.spareBtnEmail);
        createAccount = findViewById(R.id.spareRegLink);

        mFauth = FirebaseAuth.getInstance();

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                number = phoneNo.getText().toString().trim();

                Intent intent = new Intent(SparePhoneLogin.this, SpareSendOtp.class);
                intent.putExtra(SPARE_PHONE_LOGIN_NUMBER, "+27"+number);
                startActivity(intent);
                finish();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SparePhoneLogin.this, SpareRegister.class));
                finish();
            }
        });

        emailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SparePhoneLogin.this, SpareEmailLogin.class));
                finish();
            }
        });

    }
}