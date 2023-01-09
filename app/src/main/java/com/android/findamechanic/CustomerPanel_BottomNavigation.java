package com.android.findamechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.android.findamechanic.SendNotification.Token;
import com.android.findamechanic.customerPanel.CustomerHomeFragment;


import com.android.findamechanic.customerPanel.CustomerProfileFragment;
import com.android.findamechanic.customerPanel.CustomerSearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class CustomerPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_panel_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.customer_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        UpdateToken();
        String name = getIntent().getStringExtra("PAGE");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(name != null) {
            if(name.equalsIgnoreCase("HomePage")) {
                loadCustomerFragment(new CustomerHomeFragment());
            }

        } else {
            loadCustomerFragment(new CustomerHomeFragment());
        }
    }

    private void UpdateToken() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = String.valueOf(FirebaseMessaging.getInstance().getToken());
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.customer_home_ic:
                fragment = new CustomerHomeFragment();
                break;

            case R.id.customer_search_ic:
                fragment = new CustomerSearchFragment();
                break;

            case R.id.customer_profile_ic:
                fragment = new CustomerProfileFragment();
                break;
        }
        return loadCustomerFragment(fragment);
    }

    private boolean loadCustomerFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.customer_fragment_container, fragment).commit();
            return true;
        }

        return false;
    }
}