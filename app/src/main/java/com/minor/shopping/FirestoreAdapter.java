package com.minor.shopping;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class FirestoreAdapter extends FirestoreRecyclerAdapter<Product, FirestoreAdapter.ProductViewHolder> {
    private OnListItemClick onListItemClick;

    public FirestoreAdapter(@NonNull FirestoreRecyclerOptions<Product> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick=onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {
        holder.textViewPrice.setText("₹ "+model.getPrice());
        holder.textViewName.setText(model.getName());
        holder.textViewDesc.setText(model.getDesc());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Products/"+model.getName()+"/0.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.imageViewPic);
            }
        });
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single_product,parent,false);
        return new ProductViewHolder(view);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewPic;
        TextView textViewName,textViewDesc,textViewPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDesc = itemView.findViewById(R.id.textViewDescPro);
            textViewName = itemView.findViewById(R.id.textViewNamePro);
            textViewPrice = itemView.findViewById(R.id.textViewPricePro);
            imageViewPic = itemView.findViewById(R.id.imageViewProduct);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(getItem(getAdapterPosition()),getAdapterPosition());
        }
    }

    public interface OnListItemClick{
        void onItemClick(Product product, int position);
    }

}
