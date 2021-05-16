package com.minor.shopping;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class AddProduct extends Fragment {
    ImageSwitcher imageSwitcher;
    ImageButton prevButton, nextButton, addImages;
    Button buttonSave;
    ArrayList<String> imageUriList;
    private static final int PICK_IMAGES_CODE=0;
    EditText editTextName, editTextQuant, editTextDesc, editTextPrice, editTextCategory;
    //position
    int position = 0;
    String userId,cityName;
    City city;
    Associate associate;
    Product product;
    FirebaseFirestore fStore;
    FirebaseUser user;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_product, container, false);

        imageSwitcher = v.findViewById(R.id.imageSwitcher);
        prevButton = v.findViewById(R.id.imageButtonPrev);
        nextButton = v.findViewById(R.id.imageButtonNext);
        addImages = v.findViewById(R.id.buttonAddImages);
        buttonSave = v.findViewById(R.id.buttonSaveProduct);
        editTextDesc = v.findViewById(R.id.editTextDesc);
        editTextName = v.findViewById(R.id.editTextHeading);
        editTextQuant = v.findViewById(R.id.editTextQuant);
        editTextPrice = v.findViewById(R.id.editTextPrice);
        editTextCategory = v.findViewById(R.id.editTextCategory);

        imageUriList = new ArrayList<>();
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getContext());
                return imageView;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        if(user == null){
            Toast.makeText(getContext(),"Invalid login",Toast.LENGTH_SHORT).show();
            getActivity().finish();
            startActivity(new Intent(getContext(),Login.class));

        }
        userId = user.getUid();
        storageReference = FirebaseStorage.getInstance().getReference().child("Products");


        prevButton.setOnClickListener(this::gotoPrevImage);
        nextButton.setOnClickListener(this::gotoNextImage);
        addImages.setOnClickListener(view -> pickImagesIntent());
        buttonSave.setOnClickListener(view -> saveData());

        return v;
    }

    private void saveData() {
        String name = editTextName.getText().toString().trim();
        String desc = editTextDesc.getText().toString().trim();
        String cat = editTextCategory.getText().toString().trim();
        String category = cat.substring(0,1).toUpperCase()+cat.substring(1);
        List<String> tag = Arrays.asList(name.toLowerCase().split(" "));
        double price;
        long quantity;

        Validate valid = Validate.getInstance(); //validate singleton class to validate input data
        if(!valid.string(name)){
            editTextName.setError("Enter Valid Name");
            editTextName.requestFocus();
            return;
        }
        try{
            price = Double.parseDouble(editTextPrice.getText().toString().trim());
        }catch (Exception e){
            editTextPrice.setError("Enter Price");
            editTextPrice.requestFocus();
            return;
        }
        if(!valid.string(category)){
            editTextCategory.setError("Enter Description!");
            editTextCategory.requestFocus();
            return;
        }
        try{
            quantity = Long.parseLong(editTextQuant.getText().toString().trim());
        }catch (Exception e){
            editTextQuant.setError("Quantity required");
            editTextQuant.requestFocus();
            return;
        }
        if(!valid.string(desc)){
            editTextDesc.setError("Enter Description!");
            editTextDesc.requestFocus();
            return;
        }
        if(imageUriList.size()==0){
            Toast.makeText(getContext(),"No photo selected",Toast.LENGTH_SHORT).show();
            return;
        }

        //making data for object of product
        for(int i=0;i<imageUriList.size();i++){
            Uri uri = Uri.parse(imageUriList.get(i));
            StorageReference currImage = storageReference.child(name+"/"+i+".jpg");
            currImage.putFile(uri);
        }
        cityName = getActivity().getIntent().getStringExtra("City");
        product = new Product(name,desc,price,quantity,imageUriList,new ArrayList<>(),cityName,category,tag);
        fStore.collection("City").document(cityName)
                .collection("Products")
                .add(product);
        fStore.collection("Products")
                .add(product);
        fStore.collection("users")
                .document(userId)
                .collection("Products").add(product);

//        //update associate
//        DocumentReference dr = fStore.collection("users").document(userId);
//        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                associate = documentSnapshot.toObject(Associate.class);
////                assert associate != null;
//                ArrayList<String> list = associate.getProducts();
//                if(!list.contains(name)){
//                    associate.getProducts().add(name);
//                }
//                List<String> catList = associate.getCategories();
//                if(!catList.contains(category)){
//                    associate.getCategories().add(category);
//                }
////                if(!associate.getProducts().contains(name))associate.getProducts().add(name);
//                dr.set(associate);
//            }
//        });
//        //update Product
//        DocumentReference drPro = fStore.collection("Products").document(name);
//        drPro.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                product = documentSnapshot.toObject(Product.class);
//                if(product==null){
//                    product = new Product(name,desc,price,quantity,imageUriList,new ArrayList<>(),cityName,category,tag);
//                }
//                if(!product.getAssociates().contains(userId))product.getAssociates().add(userId);
//                drPro.set(product);
//            }
//        });
//        //update cities
//        DocumentReference drCit = fStore.collection("City").document(cityName);
//        drCit.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                city = documentSnapshot.toObject(City.class);
//                if(city==null){
//                    city = new City(cityName,new ArrayList<>(),new ArrayList<>());
//                }
//                if(!city.getProducts().contains(name))city.getProducts().add(name);
//                if(!city.getProducts().contains(userId))city.getAssociates().add(userId);
//                drCit.set(city);
//            }
//        });

        //restart the fragment
        Toast.makeText(getContext(),"Product Added",Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new AddProduct()).commit();
    }

    private void gotoNextImage(View view) {
        if(position<imageUriList.size()-1){
            position++;
            imageSwitcher.setImageURI(Uri.parse(imageUriList.get(position)));
        }else{
            Toast.makeText(getContext(),"Last Image",Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoPrevImage(View view) {
        if(position>0){
            position--;
            imageSwitcher.setImageURI(Uri.parse(imageUriList.get(position)));
        }else{
            Toast.makeText(getContext(),"First Image",Toast.LENGTH_SHORT).show();
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