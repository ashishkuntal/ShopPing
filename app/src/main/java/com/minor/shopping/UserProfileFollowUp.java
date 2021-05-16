package com.minor.shopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserProfileFollowUp extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 101;
    EditText editTextName, editTextCity, editTextPinCode, editTextPhone;
    ImageView imageView;
    Button button;
    Uri profileUri;
    ProgressBar progressBar;
    String profileUrl;
    FirebaseAuth mAuth;
    private boolean imageSelected;
    TextView textView;
    FirebaseFirestore fStore;
    String userId;
    StorageReference storageReference;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_follow_up);

        //initiate
        editTextName = findViewById(R.id.editTextNameAct);
        editTextCity = findViewById(R.id.editTextCityAct);
        editTextPinCode = findViewById(R.id.editTextPinCodeAct);
        editTextPhone = findViewById(R.id.editTextPhoneAct);
        button = findViewById(R.id.buttonStartAct);
        textView = findViewById(R.id.profilePicTextAct);
        imageView = findViewById(R.id.profilePicAct);

        //fbase
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = mAuth.getUid();
        user = mAuth.getCurrentUser();
        if(user==null){
            finish();
            Toast.makeText(getApplicationContext(),"Inavlid Login",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Login.class));
        }

        imageView.setOnClickListener(v -> showImageChooser());
        button.setOnClickListener(v -> saveData());
    }
    private void saveData() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String pincode = editTextPinCode.getText().toString().trim();
        Validate validate = Validate.getInstance();

        if(!validate.string(name)){
            editTextName.setError("Name required");
            return;
        }
        if(!validate.string(phone)){
            editTextPhone.setError("Cannot be empty");
            return;
        }
        if(!validate.string(city)){
            editTextCity.setError("Cannot be empty");
            return;
        }
        if(!validate.pincode(pincode)){
            editTextPinCode.setError("Enter valid pincode");
            return;
        }

        //enter data in user
        DocumentReference documentReference = fStore.collection("users").document(userId);
        User usr = new User(name,phone,pincode,city
                ,user.getEmail().toString(),"User",0,0,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
        documentReference.set(usr);
        //add name of user
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();
        user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveImageToDB() {
        ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBarAct1);
        pBar.setVisibility(View.VISIBLE);
        StorageReference fileRef = storageReference.child("users/"+userId+"/Profile.jpg");
        fileRef.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pBar.setVisibility(View.GONE);
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageView);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            profileUri = data.getData();
//            imageView.setImageURI(profileUri);
            saveImageToDB();
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,CHOOSE_IMAGE);
    }
}