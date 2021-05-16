package com.minor.shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class user_cart extends AppCompatActivity implements CartAdapter.OnListItemClick{
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    RecyclerView fStoreList;
    FirebaseUser user;
    String userId;
    CartAdapter adapter;
    Button buttonBook;
    TextView textViewHead,textViewNum,textViewPrice;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);

        fStoreList = findViewById(R.id.recyclerViewCart);
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userId = user.getUid();
        buttonBook = findViewById(R.id.button_Book);
        textViewHead = findViewById(R.id.carts_title);
        textViewNum = findViewById(R.id.cart_num);
        textViewPrice = findViewById(R.id.cart_price);
        dialog = new Dialog(this);
//        dialog.setContentView();
        checkEmpty();


        Query query = fStore.collection("users").document(userId)
                .collection("Cart");
        FirestoreRecyclerOptions<CartItem> options = new FirestoreRecyclerOptions.Builder<CartItem>()
                .setQuery(query,CartItem.class)
                .build();
        adapter = new CartAdapter(options,this);
        fStoreList.setHasFixedSize(true);
        fStoreList.setLayoutManager(new GridLayoutManager(this,3));
        fStoreList.setAdapter(adapter);

        fStore.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        User currUser = snapshot.toObject(User.class);
                        textViewNum.setText("Total Items : "+currUser.getCartNum());
                        textViewPrice.setText("Total Price : " + currUser.getCartPrice()+"₹");
                    }
                });
        buttonBook.setOnClickListener(this::book);
    }

    private void checkEmpty() {
        DocumentReference dr = fStore.collection("users").document(userId);
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                User user = snapshot.toObject(User.class);
                if(user.getCartNum()==0){
                    buttonBook.setVisibility(View.GONE);
                    textViewHead.setText("Cart is Empty");
                    textViewNum.setText("Total Items : 0 ");
                    textViewPrice.setText("Total Price : 0₹");
                }
            }
        });
    }

    private void putEmpty() {
        DocumentReference dr = fStore.collection("users").document(userId);
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                User user = snapshot.toObject(User.class);
                    buttonBook.setVisibility(View.GONE);
                    textViewHead.setText("Cart is Empty");
                    textViewNum.setText("Total Items : 0 ");
                    textViewPrice.setText("Total Price : 0₹");
            }
        });
    }

    private void book(View view) {
        fStore.collection("users").document(userId)
                .collection("Cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                CartItem cartItem = document.toObject(CartItem.class);
                                String associate = cartItem.getProduct().getAssociates().get(0);
                                BookedItem bi = new BookedItem(cartItem,userId);
                                fStore.collection("users").document(associate).collection("Booked")
                                        .add(bi);
                                fStore.collection("users").document(userId)
                                        .collection("Cart").document(document.getId()).delete();

                            }
                        }
                    }
                });
        fStore.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        User currUser = snapshot.toObject(User.class);
                        currUser.setCartNum(0);
                        currUser.setCartPrice(0);
                        fStore.collection("users").document(user.getUid()).set(currUser);
                    }
                });
        showCustomDialog();
        putEmpty();
    }
    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);

        Button ok = dialogView.findViewById(R.id.buttonOk);
        ok.setOnClickListener(this::gotoMain);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    private void gotoMain(View view) {
        finish();

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
    public void onItemClick(CartItem product, int position) {

    }
}