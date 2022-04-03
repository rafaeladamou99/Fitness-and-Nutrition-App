package com.example.fitnessandnutritionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddWeight extends AppCompatActivity {
    private NumberPicker weight1,weight2;
    int weight01,weight02;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    String userID, what;
    String[]currentweight;
    Button add,cancel;
    Intent change;
    long checktime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weight);
        getSupportActionBar().setTitle("ADD WEIGHT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        weight1 = findViewById(R.id.weight1);
        weight2 = findViewById(R.id.weight2);
        weight1.setMaxValue(300);
        weight1.setMinValue(30);
        what = getIntent().getStringExtra("Add");
        DocumentReference docRef = db.collection("Users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                   if (task.isSuccessful()) {
                                                       final DocumentSnapshot userData = task.getResult();
                                                       if (userData != null) {
                                                           ArrayList<String> weights = (ArrayList<String>) userData.get("weight");
                                                           int n = weights.size();
                                                           String lastWeight = weights.get(n-1);
                                                           String[] currentweight = lastWeight.split("time");
                                                           weight01 = (int)Double.parseDouble(currentweight[0]);
                                                           weight02 = (int)(Double.parseDouble(currentweight[0])-(int)Double.parseDouble(currentweight[0]))*10;
                                                           weight1.setValue(weight01);
                                                           weight2.setMaxValue(9);
                                                           weight2.setMinValue(0);
                                                           weight2.setValue(weight02);
                                                       }
                                                   }
                                               }
                                           });
        cancel=findViewById(R.id.cancel);
        add=findViewById(R.id.add);


        weight1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                weight01 = newValue;
            }
        });
        weight2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                weight02 = newValue;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (what.contains("target")){
                    change = new Intent(AddWeight.this, Goals.class);
                    startActivity(change);
                    overridePendingTransition(0,0);
                }
                else {
                    change = new Intent(AddWeight.this, WeightTracker.class);
                    startActivity(change);
                    overridePendingTransition(0,0);
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = db.collection("Users").document(userID);
                if (what.contains("target")){
                    String kg = weight01 + "." + weight02;
                    docRef.update("targetweight",kg);
                    change = new Intent(AddWeight.this, Goals.class);
                    startActivity(change);
                    overridePendingTransition(0,0);
                }
                else{
                    String weightntimestamp = weight01 + "." + weight02 + "time" + System.currentTimeMillis();
                    String[] timerecorded = weightntimestamp.split("time");
                    long timerec = Long.parseLong(timerecorded[1]);
                    long dif1 = checktime/86400000;
                    long dif2 = timerec/86400000;
                    if (dif2==dif1){
                        docRef.update("weight",FieldValue.arrayRemove(currentweight+"time"+checktime));
                    }
                    docRef.update("weight",FieldValue.arrayUnion(weightntimestamp));
                    change = new Intent(AddWeight.this, WeightTracker.class);
                    change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(change);
                    overridePendingTransition(0,0);
                }

            }
        });


    }
    @Override
    public boolean onSupportNavigateUp() {
        if (what.contains("target")) {
            change = new Intent(AddWeight.this, Goals.class);
            change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(change);
            overridePendingTransition(0, 0);
        }
        else{
            change = new Intent(AddWeight.this, WeightTracker.class);
            change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(change);
            overridePendingTransition(0, 0);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (what.contains("target")) {
            change = new Intent(AddWeight.this, Goals.class);
            change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(change);
            overridePendingTransition(0, 0);
        }
        else{
            change = new Intent(AddWeight.this, WeightTracker.class);
            change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(change);
            overridePendingTransition(0, 0);
        }
    }
}