package com.android.findamechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ServiceSendOtp extends AppCompatActivity {

    String verificationCode, phoneNumber;

    //Widgets
    Button verify, resend;
    TextView text;
    EditText otp;

    //Firebase
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_send_otp);

        phoneNumber = getIntent().getStringExtra(ServicePhoneLogin.SERVICE_PHONE_LOGIN_NUMBER).trim();

        otp = findViewById(R.id.sOtp);
        text = findViewById(R.id.s_text);
        verify = findViewById(R.id.s_verify_otp);
        resend = findViewById(R.id.s_resend_otp);

        mFirebaseAuth = FirebaseAuth.getInstance();

        resend.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);

        sendVerificationCode(phoneNumber);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = otp.getText().toString().trim();
                resend.setVisibility(View.INVISIBLE);

                if(code.isEmpty() && code.length()<6) {
                    otp.setError("Enter code");
                    otp.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                text.setVisibility(View.VISIBLE);
                text.setText("Resend Code within " + millisUntilFinished/1000+" Seconds");
            }

            @Override
            public void onFinish() {
                resend.setVisibility(View.VISIBLE);
                text.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null) {
                otp.setText(code); //Auto verification
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(ServiceSendOtp.this, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCode = s;
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code);
        signInWithPhone(credential);
    }

    private void signInWithPhone(PhoneAuthCredential credential) {

        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            startActivity(new Intent(ServiceSendOtp.this, ServicePanel_BottomNavigation.class));
                            finish();
                        }

                        else {
                            ReusableCodeForAll.ShowAlert(ServiceSendOtp.this, "Error", task.getException().getMessage());
                        }
                    }
                });

    }

}