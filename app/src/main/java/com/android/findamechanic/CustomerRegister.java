package com.android.findamechanic;

import static android.text.TextUtils.isEmpty;

import static com.android.findamechanic.ReusableCodeForAll.validate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

public class CustomerRegister extends AppCompatActivity {

    TextInputLayout customerName, customerEmail, customerPassword, customerConfPassword,
            customerPhoneNo, customerStreet, customerSuburb;

    Spinner cProvince;

    Button btnRegister, btnEmail, btnPhone;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String name, email, password,confirmPassword, phone, street, suburb, province;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String role = "Customer";

    public final static String CUSTOMER_REGISTER_PHONE_NUMBER =  "phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);

        //Edit Text
        customerName = findViewById(R.id.customerName);
        customerEmail = findViewById(R.id.cusRegEmail);
        customerPassword  = findViewById(R.id.cusRegPass);
        customerConfPassword = findViewById(R.id.cusConfirmPass);
        customerPhoneNo = findViewById(R.id.cusPhoneNumber);
        customerStreet = findViewById(R.id.cusStreetName);
        customerSuburb = findViewById(R.id.cusSuburb);

        //Spinners
        cProvince = findViewById(R.id.cusProvince);

        //Buttons
        btnRegister = findViewById(R.id.btnCustomerReg);
        btnEmail = findViewById(R.id.cEmailLogin);
        btnPhone = findViewById(R.id.cPhoneLogin);

        cProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Object value = adapterView.getItemAtPosition(position);
                province = value.toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        databaseReference = firebaseDatabase.getInstance().getReference("Customers");
        FAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = customerName.getEditText().getText().toString().trim();
                email = customerEmail.getEditText().getText().toString().trim();
                password = customerPassword.getEditText().getText().toString().trim();
                confirmPassword = customerConfPassword.getEditText().getText().toString().trim();
                phone = customerPhoneNo.getEditText().getText().toString().trim();
                street = customerStreet.getEditText().getText().toString().trim();
                suburb = customerSuburb.getEditText().getText().toString().trim();

                if(!isEmpty(name) && !isEmpty(email) && !isEmpty(password) && !isEmpty(confirmPassword) &&
                        !isEmpty(phone) && !isEmpty(street) && !isEmpty(suburb)){

                    if(validate(email)) {

                        FAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                Boolean isEmailEmpty = task.getResult().getSignInMethods().isEmpty();

                                if(isEmailEmpty) {
                                    if(doStringsMatch(password, confirmPassword)) {
                                        registerNewEmail(email, password);
                                    } else {
                                        Toast.makeText(CustomerRegister.this, "Passwords do not match",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(CustomerRegister.this, "Email already registered",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(CustomerRegister.this, "Please enter valid email address.",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CustomerRegister.this, "Fields cannot be empty",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerRegister.this, CustomerEmailLogin.class));
                finish();
            }
        });

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerRegister.this, CustomerPhoneLogin.class));
                finish();
            }
        });

    }

    /**
     * Register new email
     */

    private void registerNewEmail(final String email, final String password) {
        final ProgressDialog mDialog = new ProgressDialog(CustomerRegister.this);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage("Registration in progress please wait......");
        mDialog.show();

        FAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                            hashMap1.put("Customer name", name);
                            hashMap1.put("Email", email);
                            hashMap1.put("Phone number", "27"+phone);
                            hashMap1.put("Street name", street);
                            hashMap1.put("Suburb", suburb);
                            hashMap1.put("Province", province);

                            firebaseDatabase.getInstance().getReference("Customers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mDialog.dismiss();

                                            FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerRegister.this);
                                                        builder.setMessage("Registered Successfully, please click on the link sent to your email for verification");
                                                        builder.setCancelable(false);
                                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();

                                                                Intent intent = new Intent(CustomerRegister.this, CustomerVerifyPhone.class);
                                                                intent.putExtra(CUSTOMER_REGISTER_PHONE_NUMBER, "+27"+phone);
                                                                startActivity(intent);
                                                            }
                                                        });

                                                        AlertDialog Alert = builder.create();
                                                        Alert.show();
                                                    } else {
                                                        mDialog.dismiss();
                                                        ReusableCodeForAll.ShowAlert(CustomerRegister.this,"Error",task.getException().getMessage());
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
