package com.example.fitnessandnutritionapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MuscleGroupSubExercises extends AppCompatActivity implements FirestoreAdapter.OnListItemClick{

    BottomNavigationView bottomBar;
    Intent change;
    private RecyclerView muscleGroupList;
    private FirebaseFirestore db;



    private FirestoreAdapter adapter;
    String col = "MuscleGroups";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        getSupportActionBar().setTitle("EXERCISES");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.exercises);

        db = FirebaseFirestore.getInstance();
        muscleGroupList = findViewById(R.id.musclegroupslist);

        int musclegroup = getIntent().getIntExtra("keymusclegroup",1);
        int musclesubgroup = getIntent().getIntExtra("keymusclesubgroup",1);



        if (musclegroup==0){
            if(musclesubgroup==0) {
                col ="UpperChest";
            }
            else if(musclesubgroup==1) {
                col = "MiddleChest";
            }
            else if(musclesubgroup==2) {
                col = "LowerChest";
            }
        }
        else if (musclegroup==1){
            if(musclesubgroup==0) {
                col = "InnerBack";
            }
            else if(musclesubgroup==1) {
                col = "WiderBack";
            }
            else if(musclesubgroup==2) {
                col = "LowerBack";
            }
            else if(musclesubgroup==3) {
                col = "Traps";
            }
        }
        else if (musclegroup==2){
            if(musclesubgroup==0) {
                col = "FrontDeltoids";
            }
            else if(musclesubgroup==1) {
                col = "SideDeltoids";
            }
            else if(musclesubgroup==2) {
                col = "RearDeltoids";
            }
        }
        else if (musclegroup==3){
            if(musclesubgroup==0) {
                col = "Biceps";
            }
            else if(musclesubgroup==1) {
                col = "Triceps";
            }
            else if(musclesubgroup==2) {
                col = "Forearms";
            }
        }
        else if (musclegroup==4){
            if(musclesubgroup==0) {
                col = "Quadriceps";
            }
            else if(musclesubgroup==1) {
                col = "Hamstrings";
            }
            else if(musclesubgroup==2) {
                col = "Calves";
            }
            else if(musclesubgroup==3) {
                col = "Glutes";
            }
        }
        else if (musclegroup==5){
            if(musclesubgroup==0) {
                col = "UpperAbs";
            }
            else if(musclesubgroup==1) {
                col = "MiddleAbs";
            }
            else if(musclesubgroup==2) {
                col = "LowerAbs";
            }
            else if(musclesubgroup==3) {
                col = "Obliques";
            }
        }
        Query query = db.collection(col);

        final PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();


        FirestorePagingOptions<musclegroupsmodel> options = new FirestorePagingOptions.Builder<musclegroupsmodel>()
                .setLifecycleOwner(this)
                .setQuery(query,config,musclegroupsmodel.class)
                .build();
        adapter = new FirestoreAdapter(options, this);

        muscleGroupList.setHasFixedSize(true);
        muscleGroupList.setLayoutManager(new LinearLayoutManager(this));
        muscleGroupList.setAdapter(adapter);

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:
                        change = new Intent(MuscleGroupSubExercises.this, AccountActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.exercises:
                        change = new Intent(MuscleGroupSubExercises.this, ExercisesActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.workouts:
                        change = new Intent(MuscleGroupSubExercises.this, WorkoutsActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.foods:
                        change = new Intent(MuscleGroupSubExercises.this, NutritionActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.diets:
                        change = new Intent(MuscleGroupSubExercises.this, DietPlansActivity.class);
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
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        Log.d("ITEM_CLICK", "Clicked an item : " + position);
        int musclegroup = getIntent().getIntExtra("keymusclegroup",1);
        int musclesubgroup = getIntent().getIntExtra("keymusclesubgroup",1);
        change = new Intent(MuscleGroupSubExercises.this, ExerciseProfileActivity.class);
        change.putExtra("musclesubgroup",col);
        change.putExtra("keyexercise",position);
        change.putExtra("keymusclegroup",musclegroup);
        change.putExtra("keymusclesubgroup",musclesubgroup);
        startActivity(change);

    }

    @Override
    public boolean onSupportNavigateUp() {
        int musclegroup = getIntent().getIntExtra("keymusclegroup",1);
        change = new Intent(MuscleGroupSubExercises.this, MuscleGroupSubCategories.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        change.putExtra("keymusclegroup",musclegroup);
        startActivity(change);
        overridePendingTransition(0, 0);
        return true;
    }

    @Override
    public void onBackPressed() {
        int musclegroup = getIntent().getIntExtra("keymusclegroup",1);
        change = new Intent(MuscleGroupSubExercises.this, MuscleGroupSubCategories.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        change.putExtra("keymusclegroup",musclegroup);
        startActivity(change);
        overridePendingTransition(0, 0);
    }

}
