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

public class SpareEmailLogin extends AppCompatActivity {

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
        setContentView(R.layout.activity_spare_email_login);

        try {
            mEmail =  findViewById(R.id.spareEmailLogin);
            mPassword =  findViewById(R.id.sparePasswordLogin);
            btnLogin = findViewById(R.id.btnSpareLogin);
            btnPhoneLogin = findViewById(R.id.spare_phone_link_button);
            forgotPassword = findViewById(R.id.spareForgotPassword);
            register = findViewById(R.id.spareRegisterTextLink);

            mFauth = FirebaseAuth.getInstance();

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    email = mEmail.getEditText().getText().toString().trim();
                    password = mPassword.getEditText().getText().toString().trim();

                    if(!isEmpty(email) && !isEmpty(password)) {

                        if(validate(email)) {

                            final ProgressDialog mDialog = new ProgressDialog(SpareEmailLogin.this);
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
                                            Intent intent = new Intent(SpareEmailLogin.this, SparePanel_BottomNavigation.class);
                                            startActivity(intent);
                                            finish();

                                        } else  {
                                            mDialog.dismiss();
                                           ReusableCodeForAll.ShowAlert(SpareEmailLogin.this, "Verification failed.", "Please click on the link sent to your email to verify.");
                                        }
                                    } else {
                                        mDialog.dismiss();
                                        ReusableCodeForAll.ShowAlert(SpareEmailLogin.this, "Error", task.getException().getMessage());

                                    }
                                }
                            });

                        } else {
                            Toast.makeText(SpareEmailLogin.this, "Please enter a valid email address.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(SpareEmailLogin.this, "Please fill in all the fields.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SpareEmailLogin.this, SpareForgotPassword.class));
                    finish();
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SpareEmailLogin.this, SpareRegister.class));
                    finish();
                }
            });

            btnPhoneLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SpareEmailLogin.this, SparePhoneLogin.class));
                    finish();
                }
            });
        } catch (Exception e) {
            Toast.makeText(SpareEmailLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }


}
