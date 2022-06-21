package com.grpprj.cropshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import models.CropModel;

public class SaleDetailsActivity extends AppCompatActivity {
    private static final String TAG = "SaleDetailsActivity";
    ImageView ivCrop;
    TextView tvName, tvLocation, tvPrice, tvUsername, tvPhone;

    Button btnSellerCont;

    FirebaseFirestore db;

    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_details);

        ivCrop= findViewById(R.id.ivCrop);
        tvName= findViewById(R.id.tvName);
        tvPrice= findViewById(R.id.tvPrice);
        tvLocation= findViewById(R.id.tvLocation);
        tvPhone= findViewById(R.id.tvContact);
        tvUsername= findViewById(R.id.tvUsername);

        db= FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String ID = intent.getStringExtra("documentID");

        DocumentReference documentReference= db.collection("Sales").document(ID);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CropModel cropModel= documentSnapshot.toObject(CropModel.class);

                tvName.setText(cropModel.getName());
                tvLocation.setText(cropModel.getLocation());
                tvPrice.setText(cropModel.getPrice());
                Glide.with(SaleDetailsActivity.this)
                        .load(cropModel.getImageSrc())
                        .placeholder(R.drawable.ic_baseline_image_not_supported_24)
                        .centerCrop()
                        .into(ivCrop);

                DocumentReference documentReference1= db.collection("users").document(cropModel.getID());

                documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        tvPhone.setText("+263" + documentSnapshot.getString("Phone"));
                        tvUsername.setText(documentSnapshot.getString("Username"));

                        phone= documentSnapshot.getString("Phone");
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failed", e);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error fetching document", e);
            }
        });


        btnSellerCont= findViewById(R.id.btnContact);
        
        btnSellerCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSeller(phone);
            }
        });

    }

    private void callSeller(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }
}