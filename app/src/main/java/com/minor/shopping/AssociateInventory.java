package com.minor.shopping;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class AssociateInventory extends Fragment implements AdapterView.OnItemSelectedListener,FirestoreAdapter.OnListItemClick {
    //fragment physicals
    Spinner spinner;
    RecyclerView fStoreList;

    //firestore
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirestoreAdapter adapter;
    String userId;
    FirebaseFirestore fStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_associate_inventory, container, false);
        spinner = v.findViewById(R.id.spinnerInvent);
        fStoreList = v.findViewById(R.id.recyclerViewInvent);
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userId = user.getUid();
        AssociateInventory obj = this;
        //getting the cats for the user
        DocumentReference dr = fStore.collection("users").document(userId);
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Associate associate = documentSnapshot.toObject(Associate.class);
                List<String> catList = associate.getCategories();
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,catList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinner.setOnItemSelectedListener(obj);
            }
        });


        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getContext(),"Here",Toast.LENGTH_SHORT).show();
        String selectCat = parent.getItemAtPosition(position).toString();

        Query query = fStore.collection("Products")
                .whereEqualTo("category",selectCat);

        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query,Product.class)
                .build();
        adapter = new FirestoreAdapter(options,this);
        fStoreList.setHasFixedSize(true);
        fStoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        fStoreList.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(Product product, int position) {
        Intent intent = new Intent(getContext(),AssociateInventoryItemDetail.class);
        intent.putExtra("Product",product);
        startActivity(intent);
    }
}