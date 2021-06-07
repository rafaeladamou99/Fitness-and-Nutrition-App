package com.example.fitnessandnutritionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WorkoutProfile extends AppCompatActivity{

    BottomNavigationView bottomBar;
    Intent change;
    ImageView workoutimage;

    private RecyclerView mFirestoreList;
    private FirebaseFirestore firebaseFirestore;

    private WorkoutsAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerView);
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.workouts);
        workoutimage = findViewById(R.id.workoutimage);
        String id = getIntent().getStringExtra("id");

        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference workoutRef = firebaseFirestore.collection("Workouts").document(id);

        workoutRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final DocumentSnapshot doc = task.getResult();
                ArrayList<String> exercisenames = (ArrayList<String>) doc.get("exercises");
                ArrayList<String> setsrepsrest = (ArrayList<String>) doc.get("setsrepsrest");
                Picasso.get().load(doc.getString("photo")).into(workoutimage);
                getSupportActionBar().setTitle(doc.getString("name"));
                int len = setsrepsrest.size();
                String sr = null;
                String r = null;
                ArrayList<String> setsreps = new ArrayList<String>();
                ArrayList<String> rest = new ArrayList<String>();

                for (int j = 0; j<len; j++){
                    String segments[] = setsrepsrest.get(j).split("r",2);
                    sr = segments[0];
                    r = segments[1];
                    r = r +"s";
                    setsreps.add(sr);
                    rest.add(r);
                }
                WorkoutsProfileAdapter adapter = new WorkoutsProfileAdapter(WorkoutProfile.this,exercisenames, setsreps, rest);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(WorkoutProfile.this));
            }
        });






        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:
                        change = new Intent(WorkoutProfile.this, AccountActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.exercises:
                        change = new Intent(WorkoutProfile.this, ExercisesActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.workouts:
                        change = new Intent(WorkoutProfile.this, WorkoutsActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.foods:
                        change = new Intent(WorkoutProfile.this, NutritionActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.diets:
                        change = new Intent(WorkoutProfile.this, DietPlansActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        change = new Intent(WorkoutProfile.this, WorkoutsList.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String titlename = getIntent().getStringExtra("titlename");
        String category = getIntent().getStringExtra("equipment");
        change.putExtra("titlename",titlename);
        change.putExtra("equipment",category);
        startActivity(change);
        overridePendingTransition(0, 0);
        return true;
    }

    @Override
    public void onBackPressed() {
        change = new Intent(WorkoutProfile.this, WorkoutsList.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String titlename = getIntent().getStringExtra("titlename");
        String category = getIntent().getStringExtra("equipment");
        change.putExtra("titlename",titlename);
        change.putExtra("equipment",category);
        startActivity(change);
        overridePendingTransition(0, 0);
    }


}