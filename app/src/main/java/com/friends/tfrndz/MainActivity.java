package com.friends.tfrndz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.friends.tfrndz.fragments.FragmentAll;
import com.friends.tfrndz.fragments.FragmentForm;
import com.friends.tfrndz.fragments.FragmentMe;
import com.friends.tfrndz.fragments.FragmentProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new FragmentMe());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_me:
                fragment = new FragmentMe();
                break;

            case R.id.navigation_foryou:
                fragment = new FragmentForm();
                break;

            case R.id.navigation_all:
                fragment = new FragmentAll();
                break;

            case R.id.navigation_profile:
                fragment = new FragmentProfile();
                break;
        }

        return loadFragment(fragment);
    }
}
