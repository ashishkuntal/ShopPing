package com.minor.shopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AssociateAddProduct extends AppCompatActivity {
    BottomNavigationView btm_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associate_add_product);

        btm_view = findViewById(R.id.btnNavBar);
        getSupportFragmentManager().beginTransaction().add(R.id.container,new AssociateInventory()).commit();
        btm_view.setOnNavigationItemSelectedListener(this::change);
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }
    private boolean change(MenuItem v) {
        switch (v.getItemId()){
            case R.id.bottom_nav_inventory:
                loadFragment(new AssociateInventory());
                break;
            case R.id.bottom_nav_add:
                loadFragment(new AddProduct());
                break;
            case R.id.bottom_nav_settings:
                loadFragment(new AssociateSetting());
        }return true;
    }

}