package com.minor.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ItemDetailsView extends AppCompatActivity {
    TextView tvName,tvPrice,tvDesc;
    ArrayList<Uri> imageList;
    ImageButton next,prev;
    FloatingActionButton backButton;
    int pos=0;
    ImageView dummy0;
    Button buttonBook, buttonCart;
    LinearLayout linear;
    FirebaseFirestore fStore;
    FirebaseUser user;
    FirebaseAuth mAuth;
    Product product;
    User currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_view);

//        getSupportActionBar().show();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        product = (Product)getIntent().getSerializableExtra("Product");
        tvName = findViewById(R.id.textViewNameDet);
        tvPrice = findViewById(R.id.textViewPriceDet);
        tvDesc = findViewById(R.id.textViewDescDet);
        imageList = new ArrayList<>();
        dummy0 = findViewById(R.id.imageSwitcherDet);
        next = findViewById(R.id.imageButtonNext);
        prev = findViewById(R.id.imageButtonPrev);
        backButton = findViewById(R.id.floatingActionButtonBackDet);
        buttonBook = findViewById(R.id.buttonBook);
        buttonCart = findViewById(R.id.buttonCart);
        linear = findViewById(R.id.item_detail_Linear);

        tvName.setText(product.getName());
        tvPrice.setText("₹ "+product.getPrice());
        tvDesc.setText(product.getDesc());

        for(int i=0;i<product.getImages().size();i++){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Products/"+product.getName()+"/"+i+".jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(dummy0);
//                showImage();
            }
        });
        }
//        next.setOnClickListener(this::gotoNext);
//        prev.setOnClickListener(this::gotoPrev);
        backButton.setOnClickListener(this::goBack);
        buttonBook.setOnClickListener(this::openCart);
        buttonCart.setOnClickListener(this::addToCart);

    }

    private void addToCart(View view) {
        EditText etQuant = findViewById(R.id.item_detail_Quant);
        String str = etQuant.getText().toString();
        if(!Validate.getInstance().string(str)){
            etQuant.requestFocus();
            etQuant.setError("Enter Quantity");
            return;
        }
        int quant = Integer.parseInt(str);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        fStore.collection("users").document(user.getUid())
                .collection("Cart").add(new CartItem(quant,product));

        fStore.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        currUser = snapshot.toObject(User.class);
                        currUser.setCartNum(currUser.getCartNum()+1);
                        currUser.setCartPrice(currUser.getCartPrice() + quant*product.getPrice());
                        fStore.collection("users").document(user.getUid()).set(currUser);
                    }
                });
        finish();
    }

    private void openCart(View view) {
        buttonBook.setVisibility(View.GONE);
        linear.setVisibility(View.VISIBLE);
    }

    private void goBack(View v) {
        finish();
    }

//    private void showImage() {
//        imageSwitcher.setImageDrawable(dummy.getDrawable());
//    }

//    private void gotoPrev(View view) {
//        if(pos == 0)pos=imageList.size()-1;
//        else pos--;
//        imageSwitcher.setImageURI(imageList.get(pos));
//    }
//
//    private void gotoNext(View view) {
//        if(pos == imageList.size()-1)pos=0;
//        else pos++;
//        imageSwitcher.setImageURI(imageList.get(pos));
//    }
}