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

public class ExercisesActivity extends AppCompatActivity implements FirestoreAdapter.OnListItemClick {

    BottomNavigationView bottomBar;
    Intent change;
    private RecyclerView muscleGroupList;
    private FirebaseFirestore db;



    private FirestoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        getSupportActionBar().setTitle("EXERCISES");
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.exercises);

        db = FirebaseFirestore.getInstance();
        muscleGroupList = findViewById(R.id.musclegroupslist);

        Query query = db.collection("MuscleGroups");

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
                        change = new Intent(ExercisesActivity.this, AccountActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.exercises:
                        change = new Intent(ExercisesActivity.this, ExercisesActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.workouts:
                        change = new Intent(ExercisesActivity.this, WorkoutsActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.foods:
                        change = new Intent(ExercisesActivity.this, NutritionActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.diets:
                        change = new Intent(ExercisesActivity.this, DietPlansActivity.class);
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
        change = new Intent(ExercisesActivity.this, MuscleGroupSubCategories.class);
        change.putExtra("keymusclegroup",position);
        startActivity(change);

    }
}