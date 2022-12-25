package com.android.findamechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.android.findamechanic.servicePanel.ServiceHomeFragment;
import com.android.findamechanic.servicePanel.ServiceInboxFragment;
import com.android.findamechanic.servicePanel.ServiceProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ServicePanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_panel_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.service_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.serviceHome:
                fragment = new ServiceHomeFragment();
                break;

            case R.id.serviceInbox:
                fragment = new ServiceInboxFragment();
                break;

            case R.id.serviceProfile:
                fragment = new ServiceProfileFragment();
                break;
        }
        return loadServiceFragment(fragment);
    }

    private boolean loadServiceFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }

        return false;
    }
}