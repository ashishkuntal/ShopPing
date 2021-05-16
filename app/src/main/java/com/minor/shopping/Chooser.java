package com.minor.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Chooser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        findViewById(R.id.buttonChooserAssociate).setOnClickListener(v -> gotoAssociate());
        findViewById(R.id.buttonChooserCust).setOnClickListener(v -> gotoCust());
        findViewById(R.id.loginDownChooser).setOnClickListener(v -> gotoLogin());
    }

    private void gotoCust() {
        Intent intent = new Intent(getApplicationContext(),UserProfileFollowUp.class);
        startActivity(intent);
    }

    private void gotoAssociate() {
        Intent intent = new Intent(getApplicationContext(),AssociateProfileFollowUp.class);
        startActivity(intent);
    }
    private void gotoLogin() {
        Intent intent = new Intent(getApplicationContext(),Login.class);
        finish();
        startActivity(intent);
    }

}