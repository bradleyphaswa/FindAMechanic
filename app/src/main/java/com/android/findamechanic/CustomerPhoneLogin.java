package com.android.findamechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class CustomerPhoneLogin extends AppCompatActivity {

    EditText phoneNo;
    Button sendOtp, emailLogin;
    TextView createAccount;
    String number;
    FirebaseAuth mFauth;

    public final static String CUSTOMER_PHONE_LOGIN_NUMBER =  "PhoneNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_phone_login);

        phoneNo = findViewById(R.id.customerNumber);
        sendOtp = findViewById(R.id.cOtp);
        emailLogin = findViewById(R.id.cBtnEmail);
        createAccount = findViewById(R.id.cRegLink);

        mFauth = FirebaseAuth.getInstance();

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                number = phoneNo.getText().toString().trim();

                Intent intent = new Intent(CustomerPhoneLogin.this, CustomerSendOtp.class);
                intent.putExtra(CUSTOMER_PHONE_LOGIN_NUMBER,"+27"+number );
                startActivity(intent);
                finish();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerPhoneLogin.this, CustomerRegister.class));
                finish();
            }
        });

        emailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerPhoneLogin.this, CustomerEmailLogin.class));
                finish();
            }
        });
    }
}