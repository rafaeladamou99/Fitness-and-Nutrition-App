package com.example.fitnessandnutritionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Goals extends AppCompatActivity {
    TextView tkcal, tprotein, tfat, tcarbs, tweight, changeweight, changecalories, t6, t8;
    ImageView im2, im3, im4, im5, im6;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    String userID;
    Intent change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        getSupportActionBar().setTitle("GOALS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        tkcal = findViewById(R.id.targetcalories);
        tprotein = findViewById(R.id.proteintarget);
        tfat = findViewById(R.id.fattarget);
        tcarbs = findViewById(R.id.carbstarget);
        tweight = findViewById(R.id.targetweight);
        changecalories = findViewById(R.id.changetargetcalories);
        changeweight = findViewById(R.id.changetargetweight);
        im2 = findViewById(R.id.imageView2);
        im3 = findViewById(R.id.imageView3);
        im4 = findViewById(R.id.imageView4);
        im5 = findViewById(R.id.imageView5);
        im6 = findViewById(R.id.imageView6);
        t6 = findViewById(R.id.textView6);
        t8 = findViewById(R.id.textView8);
        im2.setImageResource(R.drawable.target);
        im3.setImageResource(R.drawable.target);
        im4.setImageResource(R.drawable.target);
        im5.setImageResource(R.drawable.target);
        im6.setImageResource(R.drawable.target);
        DocumentReference docRef = db.collection("Users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot userData = task.getResult();
                    if (userData != null) {
                        tweight.setText(String.valueOf(userData.get("targetweight")) + "\nkg");
                        tkcal.setText(String.valueOf(userData.get("targetkcal")) + "\nkcal");
                        tprotein.setText(String.valueOf(userData.get("protein")) + "\nprotein");
                        tcarbs.setText(String.valueOf(userData.get("carbs")) + "\ncarbs");
                        tfat.setText(String.valueOf(userData.get("fat")) + "\nfat");
                    }
                }
            }
        });
        changeweight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change = new Intent(Goals.this, AddWeight.class);
                change.putExtra("Add","target");
                change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(change);
                overridePendingTransition(0,0);
            }
        });
        changecalories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change = new Intent(Goals.this, CaloriesAdjuster.class);
                change.putExtra("Add","target");
                change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(change);
                overridePendingTransition(0,0);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        change = new Intent(Goals.this, AccountActivity.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(change);
        overridePendingTransition(0, 0);
        return true;
    }

    @Override
    public void onBackPressed() {
        change = new Intent(Goals.this, AccountActivity.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(change);
        overridePendingTransition(0, 0);
    }
}