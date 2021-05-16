
package com.minor.shopping;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserSearch extends Fragment implements FirestoreAdapter.OnListItemClick {
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    RecyclerView fStoreList;
    FirebaseUser user;
    String userId,cityName;
    FirestoreAdapter adapter;
    EditText etSearch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_associate_home, container, false);
        fStoreList = v.findViewById(R.id.recyclerViewHome);
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userId = user.getUid();
        cityName = getActivity().getIntent().getStringExtra("City").toLowerCase();
        etSearch = v.findViewById(R.id.editTextSearch);

        Query query = fStore.collection("Products");

        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query,Product.class)
                .build();
        adapter = new FirestoreAdapter(options,this);
        fStoreList.setHasFixedSize(true);
        fStoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        fStoreList.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Query query;
                if(s.toString().isEmpty()){
                    query = fStore.collection("Products");
                }else{
//                    query = fStore.collection("Products")
//                            .whereEqualTo("tag",s.toString().toLowerCase());
                    query = fStore.collection("Products")
                            .whereArrayContains("tag",s.toString().toLowerCase());
                }
                FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                        .setQuery(query,Product.class)
                        .build();
                adapter.updateOptions(options);

            }
        });
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