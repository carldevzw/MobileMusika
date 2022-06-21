package com.grpprj.cropshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    TextInputLayout txtUsername, txtPhoneNumber, txtPassword, txtConPassword;
    Button btnSubmit;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth= FirebaseAuth.getInstance();

        txtUsername= findViewById(R.id.txtUsername);
        txtPhoneNumber= findViewById(R.id.txtPhoneNumber);
        txtPassword= findViewById(R.id.txtPassword);
        txtConPassword= findViewById(R.id.txtConPassword);

        btnSubmit= findViewById(R.id.btn_Signup);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username= txtUsername.getEditText().getText().toString().trim();
                String phoneNumber= txtPhoneNumber.getEditText().getText().toString().trim();
                String password= txtPassword.getEditText().getText().toString().trim();


                if(valUsername() && valPassword() && valPasswordMatch()){

                    firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                newUserSign(username, phoneNumber, password);

                                txtUsername.getEditText().setText(null);
                                txtPhoneNumber.getEditText().setText(null);
                                txtPassword.getEditText().setText(null);
                                txtConPassword.getEditText().setText(null);

                                Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignupActivity.this, Login.class));
                            }else{
                                Toast.makeText(getApplicationContext(), "Signup Failed. Try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(SignupActivity.this, "Signup Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void newUserSign(String username, String phoneNumber, String password){

        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        String userID= firebaseUser.getUid();

        Map<String, Object> user = new HashMap<>();
        user.put("Username", username);
        user.put("Phone", "+263" + phoneNumber);
        user.put("Password", password);

        db.collection("users").document(userID)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Account Created");
                        Toast.makeText(SignupActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Sign up exception", e);
                        Toast.makeText(getApplicationContext(), "Error, try again." + e, Toast.LENGTH_LONG).show();
                    }
                });

    }

    public boolean valUsername(){

        String username= txtUsername.getEditText().getText().toString().trim();
        String checkSpace= "^(.+)@(.+)$";

        if(username.isEmpty()){
            txtUsername.setError("Required");
            return false;
        }else if(!username.matches(checkSpace)){
            txtUsername.setError("Remove whitespaces");
            return false;
        }
        else{
            txtUsername.setError(null);
            txtUsername.setErrorEnabled(false);
            return true;
        }
    }
    public boolean valPassword() {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        String password = txtPassword.getEditText().getText().toString().trim();

        if (!password.matches(PASSWORD_PATTERN)) {
            txtPassword.setError("Invalid Password");
            return false;
        } else {
            return true;

        }
    }
    public boolean valPasswordMatch(){

        String password= txtPassword.getEditText().getText().toString().trim();
        String password2= txtConPassword.getEditText().getText().toString().trim();

        if(password.isEmpty()){
            txtConPassword.setError("Required");
            return false;
        }else if(!password2.equals(password)){
            txtConPassword.setError("Passwords do not match");
            return false;
        }
        else{
            txtConPassword.setError(null);
            txtPassword.setErrorEnabled(false);
            return true;
        }
    }
}