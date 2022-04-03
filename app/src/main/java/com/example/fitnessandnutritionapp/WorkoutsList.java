package com.example.fitnessandnutritionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class WorkoutsList extends AppCompatActivity implements WorkoutsAdapter2.OnListItemClick{

    BottomNavigationView bottomBar;
    Intent change;

    private RecyclerView mFirestoreList;
    private FirebaseFirestore firebaseFirestore;

    private FirestorePagingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.workouts);

        mFirestoreList = findViewById(R.id.recycler);
        firebaseFirestore = FirebaseFirestore.getInstance();

        String titlename = getIntent().getStringExtra("titlename");
        String category = getIntent().getStringExtra("equipment");

        getSupportActionBar().setTitle(titlename + " WORKOUTS");




        Query query = firebaseFirestore.collection("Workouts").whereEqualTo("category",category);
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();

        FirestorePagingOptions<WorkoutsModel> options = new FirestorePagingOptions.Builder<WorkoutsModel>()
                .setQuery(query, config, WorkoutsModel.class)
                .build();

        adapter = new WorkoutsAdapter2(options, this);

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);



        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:
                        change = new Intent(WorkoutsList.this, AccountActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.exercises:
                        change = new Intent(WorkoutsList.this, ExercisesActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.workouts:
                        change = new Intent(WorkoutsList.this, WorkoutsActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.foods:
                        change = new Intent(WorkoutsList.this, NutritionActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.diets:
                        change = new Intent(WorkoutsList.this, DietPlansActivity.class);
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
    protected void onStop() {
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
        String id = snapshot.getId();
        String titlename = getIntent().getStringExtra("titlename");
        String category = getIntent().getStringExtra("equipment");
        Intent i = new Intent(WorkoutsList.this, WorkoutProfile.class);
        i.putExtra("id",id);
        i.putExtra("titlename",titlename);
        i.putExtra("equipment",category);
        startActivity(i);
    }
    @Override
    public boolean onSupportNavigateUp() {
        change = new Intent(WorkoutsList.this, WorkoutsActivity.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(change);
        overridePendingTransition(0, 0);
        return true;
    }

    @Override
    public void onBackPressed() {
        change = new Intent(WorkoutsList.this, WorkoutsActivity.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(change);
        overridePendingTransition(0, 0);
    }
}