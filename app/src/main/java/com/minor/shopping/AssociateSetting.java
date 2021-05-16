package com.minor.shopping;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AssociateSetting extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView etProfile,etLogOut,etInventory;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_associate_setting, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        etProfile = v.findViewById(R.id.button4);
        etLogOut = v.findViewById(R.id.button5);

        etProfile.setOnClickListener(this::gotoProfile);
        etLogOut.setOnClickListener(this::logout);
        return v;
    }

    private void gotoInventory(View view) {

    }

    private void logout(View v) {
        mAuth.signOut();
        gotoLogin();
    }
    private void gotoLogin() {
        Intent intent = new Intent(getContext(),Login.class);
        getActivity().finish();
        startActivity(intent);
    }
    private void gotoProfile(View v){
        Intent intent = new Intent(getContext(),Profile.class);
        startActivity(intent);
    }
}