package com.android.findamechanic;

import static com.android.findamechanic.ReusableCodeForAll.validate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ServiceRegister extends AppCompatActivity {

    TextInputLayout businessName, businessEmail, businessPassword, confirmPassword, phoneNumber, bStreet, bSuburb;

    Spinner bServices, bProvince;

    Button register, email, phone;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String name, emailId, password,confPassword, mobile, street, suburb, service, province;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String role = "Service";

    public final static String SERVICE_REGISTER_PHONE_NUMBER =  "mobile";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_register);

        //Edit Text
        businessName = findViewById(R.id.businessName);
        businessEmail = findViewById(R.id.servRegEmail);
        businessPassword  = findViewById(R.id.servRegPass);
        confirmPassword = findViewById(R.id.servConfirmPass);
        phoneNumber = findViewById(R.id.servPhoneNumber);
        bStreet = findViewById(R.id.servStreetName);
        bSuburb = findViewById(R.id.servSuburb);

        //Spinners
        bServices = findViewById(R.id.services);
        bProvince = findViewById(R.id.servProvince);

        //Buttons
        register = findViewById(R.id.btnServiceReg);
        email = findViewById(R.id.sEmailLogin);
        phone = findViewById(R.id.sPhoneLogin);

        bServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Object value = parent.getItemAtPosition(position);
                service = value.toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Object value = parent.getItemAtPosition(position);
                province = value.toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        databaseReference = firebaseDatabase.getInstance().getReference("Service Providers");
        FAuth = FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = businessName.getEditText().getText().toString().trim();
                emailId = businessEmail.getEditText().getText().toString().trim();
                password = businessPassword.getEditText().getText().toString().trim();
                confPassword = confirmPassword.getEditText().getText().toString().trim();
                mobile = phoneNumber.getEditText().getText().toString().trim();
                street =  bStreet.getEditText().getText().toString().trim();
                suburb = bSuburb.getEditText().getText().toString().trim();

                if(!isEmpty(name) && !isEmpty(emailId) && !isEmpty(password) && !isEmpty(confPassword) &&
                        !isEmpty(mobile) && !isEmpty(street) && !isEmpty(suburb)){

                    if(validate(emailId)) {
                           FAuth.fetchSignInMethodsForEmail(emailId).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                               @Override
                               public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                   Boolean isEmailEmpty = task.getResult().getSignInMethods().isEmpty();

                                   if(isEmailEmpty) {

                                       //Check if passwords match
                                       if(doStringsMatch(password, confPassword)) {
                                           registerNewEmail(emailId, password);
                                       } else {
                                           Toast.makeText(ServiceRegister.this, "Passwords do not match",
                                                   Toast.LENGTH_SHORT).show();
                                       }
                                   } else {
                                       Toast.makeText(ServiceRegister.this, "Email already registered",
                                               Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
                    } else {
                        Toast.makeText(ServiceRegister.this, "Please enter valid email address.",
                                Toast.LENGTH_SHORT).show();
                    }

                    } else {
                    Toast.makeText(ServiceRegister.this, "Fields cannot be empty",
                            Toast.LENGTH_SHORT).show();
                }



            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServiceRegister.this, ServiceEmailLogin.class));
                finish();
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServiceRegister.this, ServicePhoneLogin.class));
                finish();
            }
        });

        }

    /**
     * Register new email
     */

    private void registerNewEmail(final String email, final String password) {
        final ProgressDialog mDialog = new ProgressDialog(ServiceRegister.this);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage("Registration in progress please wait......");
        mDialog.show();

        FAuth.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId);
                    final HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("Role", role);
                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            HashMap<String, String> hashMap1 = new HashMap<>();
                            hashMap1.put("Business name", name);
                            hashMap1.put("Service", service);
                            hashMap1.put("Email", emailId);
                            hashMap1.put("Phone number", "27"+mobile);
                            hashMap1.put("Street name", street);
                            hashMap1.put("Suburb", suburb);
                            hashMap1.put("Province", province);

                            firebaseDatabase.getInstance().getReference("Service Providers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mDialog.dismiss();

                                            FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceRegister.this);
                                                        builder.setMessage("Registered Successfully, please click on the link sent to your email for verification");
                                                        builder.setCancelable(false);
                                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();

                                                                Intent intent = new Intent(ServiceRegister.this, ServiceVerifyPhone.class);
                                                                intent.putExtra(SERVICE_REGISTER_PHONE_NUMBER, "+27"+mobile);
                                                                startActivity(intent);
                                                            }
                                                        });

                                                        AlertDialog Alert = builder.create();
                                                        Alert.show();
                                                    } else {
                                                        mDialog.dismiss();
                                                        ReusableCodeForAll.ShowAlert(ServiceRegister.this,"Error",task.getException().getMessage());
                                                    }
                                                }
                                            });
                                        }
                                    });
                        }
                    });
                }
            }
        });
    }



    private boolean isEmpty(String string){
        return string.equals("");
    }

    private boolean doStringsMatch(String s1, String s2){
        return s1.equals(s2);
    }
    }

