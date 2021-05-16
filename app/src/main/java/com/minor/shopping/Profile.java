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

public class Profile extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView editTextName, editTextEmail, editTextPhone, editTextAccType, editTextCity;
    ImageView imageViewProfile;
    String name,userId;
    FirebaseUser user;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextName = findViewById(R.id.textViewNameMain);
        editTextPhone = findViewById(R.id.textViewPhoneMain);
        editTextEmail = findViewById(R.id.textViewEmailMain);
        editTextAccType = findViewById(R.id.textViewAccMain);
        imageViewProfile = findViewById(R.id.imageViewProfilePic);
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
        userId = user.getUid();
        showData();
    }
    private void showData() {

        //show profile pic
        StorageReference picRef = storageReference.child("users/"+userId+"/Profile.jpg");
        picRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageViewProfile);
            }
        });

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Associate associate = documentSnapshot.toObject(Associate.class);
                if(associate==null)return;
                editTextName.setText(associate.getName());
                editTextEmail.setText(associate.getEmail());
                editTextAccType.setText(associate.getAccType());
                editTextPhone.setText(associate.getPhone());
                editTextCity.setText(associate.getCity());

            }
        });


    }
    private void gotoLogin() {
        Intent intent = new Intent(this,Login.class);
        finish();
        startActivity(intent);
    }
}