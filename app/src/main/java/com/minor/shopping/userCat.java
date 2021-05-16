package com.minor.shopping;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class userCat extends Fragment {
    LinearLayout food,furniture,sports,cloths,general,electronics;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_user_cat, container, false);
        food = v.findViewById(R.id.cats_food);
        sports = v.findViewById(R.id.cats_gym);
        furniture = v.findViewById(R.id.cats_furniture);
        cloths = v.findViewById(R.id.cats_cloths);
        general = v.findViewById(R.id.cats_general);
        electronics = v.findViewById(R.id.cats_electronics);

        food.setOnClickListener(view->loadFragment(new userCatShow("Food")));
        sports.setOnClickListener(view->loadFragment(new userCatShow("Sports")));
        furniture.setOnClickListener(view->loadFragment(new userCatShow("Furniture")));
        cloths.setOnClickListener(view->loadFragment(new userCatShow("Cloths")));
        general.setOnClickListener(view->loadFragment(new userCatShow("General")));
        electronics.setOnClickListener(view->loadFragment(new userCatShow("Electronics")));
        return v;
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.container,fragment);
        fragmentTransaction.commit();
    }
}