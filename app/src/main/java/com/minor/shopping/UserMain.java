package com.minor.shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserMain extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    BottomNavigationView btm_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        btm_view = findViewById(R.id.btnNavBarUsr);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        getSupportFragmentManager().beginTransaction().add(R.id.container,new UserHome()).commit();
        btm_view.setOnNavigationItemSelectedListener(this::change);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuProfile:
                gotoProfile();
                break;
            case R.id.menuLogout:
                logout();
                break;
            case R.id.menuCart:
                openCart();
                break;
        }
        return true;
    }

    private void openCart() {
        Intent intent = new Intent(this,user_cart.class);
        startActivity(intent);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }
    private boolean change(MenuItem v) {
        switch (v.getItemId()){
            case R.id.bottom_nav_home:
                loadFragment(new UserHome());
                break;
            case R.id.bottom_nav_search:
                loadFragment(new UserSearch());
                break;
        }return true;
    }private void logout() {
        mAuth.signOut();
        gotoLogin();
    }
    private void gotoLogin() {
        Intent intent = new Intent(this,Login.class);
        finish();
        startActivity(intent);
    }
    private void gotoProfile(){
        Intent intent = new Intent(this,Profile.class);
        startActivity(intent);
    }
}