package com.example.fitnessandnutritionapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText emailId, password, age, weight, height;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fStore;
    String userID;
    Spinner gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        emailId = findViewById(R.id.TextEmailAddress);
        password = findViewById(R.id.TextPassword);
        age = findViewById(R.id.age);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        tvSignIn = findViewById(R.id.LoginLink);
        btnSignUp = findViewById(R.id.SignUp);
        gender = findViewById(R.id.gender);
        ArrayAdapter<String> adaptergoal = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.gender));
        adaptergoal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adaptergoal);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                final String uAge = age.getText().toString();
                final String uWeight = weight.getText().toString();
                final String uHeight = height.getText().toString();

                if (email.isEmpty()) {
                    emailId.setError("Please provide your email addess");
                    emailId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if(uAge.isEmpty()){
                    age.setError("Please enter your password");
                    age.requestFocus();
                }
                else if(uHeight.isEmpty()){
                    height.setError("Please enter your password");
                    height.requestFocus();
                }
                else if(uWeight.isEmpty()){
                    weight.setError("Please enter your password");
                    weight.requestFocus();
                }
                else if(email.isEmpty() && pwd.isEmpty() && uAge.isEmpty() && uHeight.isEmpty() && uWeight.isEmpty()){
                    Toast.makeText(SignupActivity.this, "Fields are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty() && uAge.isEmpty() && uHeight.isEmpty() && uWeight.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignupActivity.this, "Sign Up Unsuccessful, Please try Again!",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                userID = mFirebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("Users").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                List<String> weight = new ArrayList<String>();
                                List<String> food = new ArrayList<String>();
                                String weightntimestamp = uWeight + ".0" + "time" + System.currentTimeMillis();
                                weight.add(weightntimestamp);
                                String genderselected = gender.getSelectedItem().toString().toLowerCase();
                                user.put("age",uAge);
                                user.put("height",uHeight);
                                user.put("weight",weight);
                                user.put("breakfast",food);
                                user.put("lunch",food);
                                user.put("dinner",food);
                                user.put("snack",food);
                                user.put("carbs",0+"");
                                user.put("protein",0+"");
                                user.put("fat",0+"");
                                user.put("gender",genderselected);
                                user.put("targetkcal",0+"");
                                user.put("targetweight",0+"");
                                user.put("permission-level",1+"");
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("ONSUCCESS: ", "User: " + userID + " created.");
                                    }
                                });
                                startActivity(new Intent(SignupActivity.this, AccountActivity.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SignupActivity.this, "Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}