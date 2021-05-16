package com.minor.shopping;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AssociateUpdateProduct extends AppCompatActivity {
    Product product;
    ImageView imageSwitcher;
    ImageButton prevButton, nextButton, addImages;
    Button buttonSave;
    ArrayList<String> imageUriList;
    private static final int PICK_IMAGES_CODE=0;
    EditText editTextName, editTextQuant, editTextDesc, editTextPrice, editTextCategory;
    //position
    int position = 0;
    FirebaseFirestore fStore;
    String userId,cityName;
    City city;
    Associate associate;
    FirebaseUser user;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associate_update_product);

        product = (Product)getIntent().getSerializableExtra("Product");

        imageSwitcher = findViewById(R.id.imageSwitcher);
        prevButton = findViewById(R.id.imageButtonPrev);
        nextButton = findViewById(R.id.imageButtonNext);
        addImages = findViewById(R.id.buttonAddImages);
        buttonSave = findViewById(R.id.buttonSaveProduct);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextName = findViewById(R.id.editTextHeading);
        editTextQuant = findViewById(R.id.editTextQuant);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextCategory = findViewById(R.id.editTextCategory);

        imageUriList = product.getImages();
        imageSwitcher.setImageURI(Uri.parse(imageUriList.get(0)));
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Products/"+product.getName()+"/"+0+".jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageSwitcher);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        if(user == null){
            Toast.makeText(this,"Invalid login",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this,Login.class));

        }
        userId = user.getUid();


        prevButton.setOnClickListener(this::gotoPrevImage);
        nextButton.setOnClickListener(this::gotoNextImage);
        addImages.setOnClickListener(view -> pickImagesIntent());
        buttonSave.setOnClickListener(view -> saveData());


        //set previous data in existing buttons
//        editTextDesc.setText(product.getDesc());
        editTextName.setText(product.getName());
        editTextQuant.setText(product.getQuantity()+"");
        editTextPrice.setText(product.getPrice()+"");
        editTextCategory.setText(product.getCategory());
        editTextDesc.setText(product.getDesc());
    }

    private void saveData() {
        DocumentReference drPro = fStore.collection("Products").document(product.getName());
        product.setDesc(editTextDesc.getText().toString());
        product.setQuantity(Long.parseLong(editTextQuant.getText().toString()));
        product.setPrice(Double.parseDouble(editTextPrice.getText().toString()));
        product.setCategory(editTextCategory.getText().toString());
        drPro.set(product);
        finish();
    }

    private void gotoNextImage(View view) {
        if(position<imageUriList.size()-1){
            position++;
            imageSwitcher.setImageURI(Uri.parse(imageUriList.get(position)));
        }else{
            Toast.makeText(this,"Last Image",Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoPrevImage(View view) {
        if(position>0){
            position--;
            imageSwitcher.setImageURI(Uri.parse(imageUriList.get(position)));
        }else{
            Toast.makeText(this,"First Image",Toast.LENGTH_SHORT).show();
        }
    }

    private void pickImagesIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image(s)"),PICK_IMAGES_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGES_CODE && resultCode == Activity.RESULT_OK){
            if(data.getClipData() !=null){
                int cnt = data.getClipData().getItemCount();
                for(int i=0;i<cnt;i++){
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    imageUriList.add(uri.toString());
                }
                imageSwitcher.setImageURI(Uri.parse(imageUriList.get(0)));
                position=0;
            }else{
                Uri uri = data.getData();
                imageUriList.add(uri.toString());
                imageSwitcher.setImageURI(uri);
                position++;
            }
        }
    }
}