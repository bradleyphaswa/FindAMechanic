package com.android.findamechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.android.findamechanic.SendNotification.Token;
import com.android.findamechanic.sparePanel.SpareHomeFragment;
import com.android.findamechanic.sparePanel.SpareInboxFragment;
import com.android.findamechanic.sparePanel.SpareProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class SparePanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spare_panel_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.spare_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        UpdateToken();
        loadServiceFragment(new SpareHomeFragment());
        String name = getIntent().getStringExtra("PAGE");
        if(name !=null) {
             if (name.equalsIgnoreCase("AcceptRequest")) {
                loadServiceFragment(new SpareInboxFragment());
            } else {
                loadServiceFragment(new SpareHomeFragment());
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.spareHome:
                fragment = new SpareHomeFragment();
                break;

            case R.id.spareInbox:
                fragment = new SpareInboxFragment();
                break;

            case R.id.spareProfile:
                fragment = new SpareProfileFragment();
                break;
        }
        return loadServiceFragment(fragment);
    }

    private boolean loadServiceFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.spare_fragment_container, fragment).commit();
            return true;
        }

        return false;
    }

    private void UpdateToken() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = String.valueOf(FirebaseMessaging.getInstance().getToken());
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

    private boolean loadSpareFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.spare_fragment_container, fragment).commit();
            return true;
        }

        return false;
    }
}