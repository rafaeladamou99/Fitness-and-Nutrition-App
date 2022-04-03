package com.example.fitnessandnutritionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    BottomNavigationView bottomBar;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseFirestore db;
    Intent change;
    Button weightTracker, goals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().setTitle("ACCOUNT");
        db = FirebaseFirestore.getInstance();
        weightTracker = findViewById(R.id.button2);
        weightTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountActivity.this, WeightTracker.class);
                startActivity(i);
            }
        });
        goals = findViewById(R.id.button3);
        goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountActivity.this, Goals.class);
                startActivity(i);
            }
        });

        //push check


        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.account);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.account:
                        change = new Intent(AccountActivity.this, AccountActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.exercises:
                        change = new Intent(AccountActivity.this, ExercisesActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.workouts:
                        change = new Intent(AccountActivity.this, WorkoutsActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.foods:
                        change = new Intent(AccountActivity.this, NutritionActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.diets:
                        change = new Intent(AccountActivity.this, DietPlansActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });


    }

    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intToMain = new Intent(AccountActivity.this, SignupActivity.class);
        startActivity(intToMain);
    }
}