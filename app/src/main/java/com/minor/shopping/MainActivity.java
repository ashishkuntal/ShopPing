package com.minor.shopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView editTextAccType, editTextCity;
    String name,userId;
    FirebaseUser user;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    Button buttonHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextAccType = findViewById(R.id.textViewAccMain);
        editTextCity = findViewById(R.id.textViewCityMain);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        storageReference  = FirebaseStorage.getInstance().getReference();

        if(user==null){
            gotoLogin();
            return;
        }
        name = user.getDisplayName();
        if(name == null || name.length()==0){
            gotoChooser();
        }
        userId = user.getUid();
        showData();
//        buttonHome.setOnClickListener(v ->{
//            if(editTextAccType.getText().toString().trim().equals("Associate")){
//                gotoAssociateAddProduct();
//            }
//        });
    }

    private void gotoAssociateAddProduct() {
        Intent intent = new Intent(getApplicationContext(),AssociateAddProduct.class);
        intent.putExtra("City",editTextCity.getText().toString().trim());
        finish();
        startActivity(intent);
    }

    private void showData() {

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Associate associate = documentSnapshot.toObject(Associate.class);
                if(associate==null)return;
                editTextAccType.setText(associate.getAccType());
                editTextCity.setText(associate.getCity());
                if(editTextAccType.getText().toString().trim().equals("Associate")){
                    gotoAssociateAddProduct();
                }else{
                    gotoUser();
                }
            }
        });


    }

    private void gotoUser() {
        Intent intent = new Intent(getApplicationContext(),UserMain.class);
        intent.putExtra("City",editTextCity.getText().toString().trim());
        finish();
        startActivity(intent);
    }

    private void gotoChooser() {
        Intent intent = new Intent(this,Chooser.class);
        finish();
        startActivity(intent);
    }

    private void gotoLogin() {
        Intent intent = new Intent(this,Login.class);
        finish();
        startActivity(intent);
    }
}