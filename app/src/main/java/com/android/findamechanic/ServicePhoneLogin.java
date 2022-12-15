package com.android.findamechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ServicePhoneLogin extends AppCompatActivity {

    EditText phoneNo;
    Button sendOtp, emailLogin;
    TextView createAccount;
    String number;
    FirebaseAuth mFauth;

    public final static String SERVICE_PHONE_LOGIN_NUMBER =  "PhoneNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_phone_login);

        phoneNo = findViewById(R.id.serviceNumber);
        sendOtp = findViewById(R.id.sOtp);
        emailLogin = findViewById(R.id.sBtnEmail);
        createAccount = findViewById(R.id.sRegLink);

        mFauth = FirebaseAuth.getInstance();

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                number = phoneNo.getText().toString().trim();

                Intent intent = new Intent(ServicePhoneLogin.this, ServiceSendOtp.class);
                intent.putExtra(SERVICE_PHONE_LOGIN_NUMBER, "+27"+number);
                startActivity(intent);
                finish();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServicePhoneLogin.this, ServiceRegister.class));
                finish();
            }
        });

        emailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServicePhoneLogin.this, ServiceEmailLogin.class));
                finish();
            }
        });

    }
}