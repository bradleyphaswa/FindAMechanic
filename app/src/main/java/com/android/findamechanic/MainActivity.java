package com.android.findamechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth Fauth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Fauth = FirebaseAuth.getInstance();
                if(Fauth.getCurrentUser()!=null) {
                    if(Fauth.getCurrentUser().isEmailVerified()) {
                        Fauth = FirebaseAuth.getInstance();

                        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getUid()+"/Role");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String role = snapshot.getValue(String.class);
                                if(role.equals("Mechanic")) {
                                    startActivity(new Intent(MainActivity.this, SparePanel_BottomNavigation.class));
                                    finish();
                                }

                                if(role.equals("Spares")) {
                                    startActivity(new Intent(MainActivity.this, SparePanel_BottomNavigation.class));
                                    finish();
                                }

                                if(role.equals("Car Dealer")) {
                                    startActivity(new Intent(MainActivity.this, SparePanel_BottomNavigation.class));
                                    finish();
                                }

                                if(role.equals("Customer")) {
                                    startActivity(new Intent(MainActivity.this, CustomerPanel_BottomNavigation.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Email provided is not verified. Please check your inbox and follow the link to verify.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        Fauth.signOut();
                    }
                } else {

                    Intent intent = new Intent(MainActivity.this, MainMenu.class);
                    startActivity(intent);
                    finish();

                }
            }
        }, 3000);
    }
}