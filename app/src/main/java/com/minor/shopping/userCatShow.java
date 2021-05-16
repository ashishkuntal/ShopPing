package com.minor.shopping;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class userCatShow extends Fragment implements FirestoreAdapter.OnListItemClick{
    String cat;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    RecyclerView fStoreList;
    FirebaseUser user;
    String userId;
    FirestoreAdapter adapter;
    TextView cats_title;
    public userCatShow(String cat) {
        this.cat = cat;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_user_cat_show, container, false);

        cats_title = v.findViewById(R.id.cats_title);
        cats_title.setText(cat);
        fStoreList = v.findViewById(R.id.recyclerViewHome);
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userId = user.getUid();
        Query query = fStore.collection("Products")
                .whereArrayContains("tag",cat.toLowerCase());
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query,Product.class)
                .build();
        adapter = new FirestoreAdapter(options,this);
        fStoreList.setHasFixedSize(true);
        fStoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        fStoreList.setAdapter(adapter);
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    public void onItemClick(Product product, int position) {
        Intent intent = new Intent(getContext(),ItemDetailsView.class);
        intent.putExtra("Product",product);
        startActivity(intent);
    }
}