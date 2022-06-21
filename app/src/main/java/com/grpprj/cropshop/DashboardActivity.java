package com.grpprj.cropshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import adapters.CropsAdapter;
import models.CropModel;

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "DashboardActivity";

    Button btnAdd;

    private ArrayList<CropModel> cropModelArrayList;
    RecyclerView rvCrops;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnAdd= findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, AddMealActivity.class));
            }
        });

        listCrops();

    }

    public void listCrops(){

        cropModelArrayList = new ArrayList<>();

        rvCrops= findViewById(R.id.rvCropItems);
        rvCrops.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        rvCrops.setLayoutManager(linearLayoutManager);


        db= FirebaseFirestore.getInstance();

        db.collection("Sales")
                .orderBy("Date")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e(TAG, "Fetch may have returned null", error);
                        }else {
                            assert value != null;
                            for(DocumentChange dc: value.getDocumentChanges()){
                                if(dc.getType()== DocumentChange.Type.ADDED){
                                    cropModelArrayList.add(dc.getDocument().toObject(CropModel.class));
                                }
                                CropsAdapter cropsAdapter = new CropsAdapter(DashboardActivity.this, cropModelArrayList);
                                rvCrops.setAdapter(cropsAdapter);
                                Log.i(TAG, "Fetched");
                                cropsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}