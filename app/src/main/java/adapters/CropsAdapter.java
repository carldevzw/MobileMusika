package adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.grpprj.cropshop.R;
import com.grpprj.cropshop.SaleDetailsActivity;

import java.util.ArrayList;

import models.CropModel;
import viewholders.CropsViewholder;

public class CropsAdapter extends RecyclerView.Adapter<CropsViewholder> {

    private static final String TAG = "Meal_Orders_Adapter";
    private Context context;
    private ArrayList<CropModel> CropModelArrayList;
    FirebaseFirestore db;

    public CropsAdapter(Context context, ArrayList<CropModel> cropModelArrayList) {
        this.context = context;
        CropModelArrayList = cropModelArrayList;
    }

   /* public void deleteFromCart(int position){
        CropModel model = CropModelArrayList.get(position);
        db= FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String userId= firebaseUser.getUid();

        CollectionReference collectionReference= db.collection("users").document(userId).collection("orders");
        DocumentReference documentReference= db.collection("users").document(userId).collection("orders").document(model.getDocumentID());

        documentReference.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Order removed from database.");
                        }
                    }
                });
    }*/

    @NonNull
    @Override
    public CropsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crop_item, parent, false);
        return new CropsViewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CropsViewholder holder, int position) {

        CropModel model = CropModelArrayList.get(position);
        holder.tvName.setText(model.getName());
        holder.tvPrice.setText(model.getPrice());
        Glide.with(context)
                .load(model.getImageSrc())
                .placeholder(R.drawable.ic_baseline_image_not_supported_24)
                .centerCrop()
                .into(holder.ivCrop);
        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(CropsAdapter.this.context, SaleDetailsActivity.class).putExtra("documentID", model.getDocumentId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return CropModelArrayList.size();
    }
}

