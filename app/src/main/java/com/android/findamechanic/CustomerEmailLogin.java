package com.android.findamechanic;

import static android.text.TextUtils.isEmpty;
import static com.android.findamechanic.ReusableCodeForAll.validate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerEmailLogin extends AppCompatActivity {

    //constants
    private static final int ERROR_DIALOG_REQUEST = 9001;

    //Firebase
    private FirebaseAuth mFauth;

    // widgets
    private TextInputLayout mEmail, mPassword;
    private String email, password;
    private Button btnLogin, btnPhoneLogin;
    private TextView forgotPassword, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_email_login);

        try {

            mEmail =  findViewById(R.id.customerEmailLogin);
            mPassword =  findViewById(R.id.customerPasswordLogin);
            btnLogin = findViewById(R.id.btnCustomerLogin);
            btnPhoneLogin = findViewById(R.id.c_phone_link_button);
            forgotPassword = findViewById(R.id.cForgotPassword);
            register = findViewById(R.id.cRegisterTextLink);

            mFauth = FirebaseAuth.getInstance();

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    email = mEmail.getEditText().getText().toString().trim();
                    password = mPassword.getEditText().getText().toString().trim();

                    if(!isEmpty(email) && !isEmpty(password)) {

                        if(validate(email)) {

                            final ProgressDialog mDialog = new ProgressDialog(CustomerEmailLogin.this);
                            mDialog.setCancelable(false);
                            mDialog.setCanceledOnTouchOutside(false);
                            mDialog.setMessage("Please wait......");
                            mDialog.show();

                            mFauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        mDialog.dismiss();

                                        if(mFauth.getCurrentUser().isEmailVerified()) {

                                            mDialog.dismiss();
                                            Intent intent = new Intent(CustomerEmailLogin.this, CustomerPanel_BottomNavigation.class);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            mDialog.dismiss();
                                            ReusableCodeForAll.ShowAlert(CustomerEmailLogin.this, "Verification failed.", "Please click on the link sent to your email to verify.");

                                        }
                                    } else {
                                        mDialog.dismiss();
                                        ReusableCodeForAll.ShowAlert(CustomerEmailLogin.this, "Error", task.getException().getMessage());

                                    }
                                }
                            });

                        } else {
                            Toast.makeText(CustomerEmailLogin.this, "Please enter a valid email address.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(CustomerEmailLogin.this, "Please fill in all the fields.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(CustomerEmailLogin.this, CustomerForgotPassword.class));
                    finish();
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(CustomerEmailLogin.this, CustomerRegister.class));
                    finish();
                }
            });

            btnPhoneLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(CustomerEmailLogin.this, CustomerPhoneLogin.class));
                    finish();
                }
            });

        } catch(Exception e) {
            Toast.makeText(CustomerEmailLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}