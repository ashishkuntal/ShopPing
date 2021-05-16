package com.minor.shopping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AssociateInventoryItemDetail extends AppCompatActivity {
    TextView tvName,tvPrice,tvDesc,tvStock;
    ArrayList<Uri> imageList;
    ImageButton next,prev;
    FloatingActionButton backButton;
    int pos=0;
    ImageView dummy0;
    Button buttonUpdate, buttonRemove;
    Product product;

    FirebaseFirestore fStore;
    FirebaseUser user;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associate_inventory_item_detail);

        product = (Product)getIntent().getSerializableExtra("Product");
        tvName = findViewById(R.id.textViewNameDet);
        tvPrice = findViewById(R.id.textViewPriceDet);
        tvDesc = findViewById(R.id.textViewDescDet);
        imageList = new ArrayList<>();
        dummy0 = findViewById(R.id.imageSwitcherDet);
        next = findViewById(R.id.imageButtonNext);
        prev = findViewById(R.id.imageButtonPrev);
        backButton = findViewById(R.id.floatingActionButtonBackDet);
        tvStock = findViewById(R.id.textViewInStock);
        buttonRemove = findViewById(R.id.buttonRemoveItem);
        buttonUpdate = findViewById(R.id.buttonUpdateItem);

        //firestore
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        tvName.setText(product.getName());
        tvPrice.setText("₹ "+product.getPrice());
        tvDesc.setText(product.getDesc());
        tvStock.setText("Item left : "+Long.toString(product.getQuantity()));

        buttonUpdate.setOnClickListener(this::update);
        buttonRemove.setOnClickListener(this::remove);
        backButton.setOnClickListener(this::goBack);


        //putting image
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Products/"+product.getName()+"/0.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(dummy0));
    }
    private void goBack(View view) {
        finish();
    }

    private void remove(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Remove");
        builder.setMessage("Proceed?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DocumentReference drPro = fStore.collection("Products").document(product.getName());
                drPro.delete();
                DocumentReference dr = fStore.collection("users").document(user.getUid());
                dr.get().addOnSuccessListener(documentSnapshot -> {
                    Associate associate = documentSnapshot.toObject(Associate.class);
                    associate.getProducts().remove(product.getName());
                    dr.set(associate);
                });
                goBack(view);
            }
        }).setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void update(View view) {
        Intent intent = new Intent(this,AssociateUpdateProduct.class);
        intent.putExtra("Product",product);
        finish();
        startActivity(intent);

    }
}