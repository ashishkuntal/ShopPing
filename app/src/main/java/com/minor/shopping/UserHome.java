package com.minor.shopping;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class UserHome extends Fragment {
    LinearLayout cats,purchased,popular,recommended;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_home, container, false);
        cats = v.findViewById(R.id.categories);
        purchased = v.findViewById(R.id.purchased);
        popular = v.findViewById(R.id.popular);
        recommended = v.findViewById(R.id.recommended);
        
        cats.setOnClickListener(view -> loadFragment(new userCat()));
        purchased.setOnClickListener(view -> loadFragment(new userCat()));
        popular.setOnClickListener(view -> loadFragment(new userCat()));
        recommended.setOnClickListener(view -> loadFragment(new userCat()));
        return v;
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.container,fragment);
        fragmentTransaction.commit();
    }

}