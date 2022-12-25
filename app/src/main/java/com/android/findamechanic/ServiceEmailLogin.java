package com.android.findamechanic;

import static android.text.TextUtils.isEmpty;

import static com.android.findamechanic.ReusableCodeForAll.validate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ServiceEmailLogin extends AppCompatActivity {

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
        setContentView(R.layout.activity_service_email_login);

        try {
            mEmail =  findViewById(R.id.serviceEmailLogin);
            mPassword =  findViewById(R.id.servicePasswordLogin);
            btnLogin = findViewById(R.id.btnServiceLogin);
            btnPhoneLogin = findViewById(R.id.s_phone_link_button);
            forgotPassword = findViewById(R.id.sForgotPassword);
            register = findViewById(R.id.sRegisterTextLink);

            mFauth = FirebaseAuth.getInstance();

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    email = mEmail.getEditText().getText().toString().trim();
                    password = mPassword.getEditText().getText().toString().trim();

                    if(!isEmpty(email) && !isEmpty(password)) {

                        if(validate(email)) {

                            final ProgressDialog mDialog = new ProgressDialog(ServiceEmailLogin.this);
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
                                            Intent intent = new Intent(ServiceEmailLogin.this, ServicePanel_BottomNavigation.class);
                                            startActivity(intent);
                                            finish();

                                        } else  {
                                            mDialog.dismiss();
                                           ReusableCodeForAll.ShowAlert(ServiceEmailLogin.this, "Verification failed.", "Please click on the link sent to your email to verify.");
                                        }
                                    } else {
                                        mDialog.dismiss();
                                        ReusableCodeForAll.ShowAlert(ServiceEmailLogin.this, "Error", task.getException().getMessage());

                                    }
                                }
                            });

                        } else {
                            Toast.makeText(ServiceEmailLogin.this, "Please enter a valid email address.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ServiceEmailLogin.this, "Please fill in all the fields.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ServiceEmailLogin.this, ServiceForgotPassword.class));
                    finish();
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ServiceEmailLogin.this, ServiceRegister.class));
                    finish();
                }
            });

            btnPhoneLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ServiceEmailLogin.this, ServicePhoneLogin.class));
                    finish();
                }
            });
        } catch (Exception e) {
            Toast.makeText(ServiceEmailLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }


}
