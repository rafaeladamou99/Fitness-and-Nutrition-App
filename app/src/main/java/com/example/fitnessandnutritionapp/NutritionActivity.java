package com.example.fitnessandnutritionapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

public class NutritionActivity extends AppCompatActivity implements FirestoreAdapterFoods.OnListItemClick {

    BottomNavigationView bottomBar;
    Intent change;
    EditText searchText;
    ImageButton searchButton,add;
    private RecyclerView muscleGroupList;
    private FirebaseFirestore db;

    public FirestoreAdapterFoods adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        getSupportActionBar().setTitle("FOODS");
        String foodcategory = getIntent().getStringExtra("foodcategory");
        if (foodcategory==null){
            foodcategory = "nothing";
        }
        if (foodcategory.contains("breakfast") || foodcategory.contains("lunch") || foodcategory.contains("dinner") || foodcategory.contains("snack"))
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.foods);
        searchText = findViewById(R.id.search_bar);
        searchButton = findViewById(R.id.search_button);
        db = FirebaseFirestore.getInstance();
        muscleGroupList = findViewById(R.id.musclegroupslist);
        add = findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NutritionActivity.this, AddFoodActivity.class);
                String foodcategory = getIntent().getStringExtra("foodcategory");
                if (foodcategory==null){
                    foodcategory = "nothing";
                }
                i.putExtra("foodcategory", foodcategory);
                startActivity(i);
            }
        });

        Query query = db.collection("Empty");

        final PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();

        FirestorePagingOptions<foodsmodel> options = new FirestorePagingOptions.Builder<foodsmodel>()
                .setLifecycleOwner(this)
                .setQuery(query, config,foodsmodel.class)
                .build();
        adapter = new FirestoreAdapterFoods(options, this);

        muscleGroupList.setHasFixedSize(true);
        muscleGroupList.setLayoutManager(new LinearLayoutManager(this));
        muscleGroupList.setAdapter(adapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = searchText.getText().toString();
                search = search.toLowerCase();
                if (search.isEmpty()){
                    //do Nothing
                }
                else{
                    Query query = db.collection("Foods")
                            .whereArrayContains("keywords",search).limit(10);
                    FirestorePagingOptions<foodsmodel> options = new FirestorePagingOptions.Builder<foodsmodel>()
                            //.setLifecycleOwner(this)
                            .setQuery(query, config, foodsmodel.class)
                            .build();
                    adapter.updateOptions(options);
                }

            }
        });








        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:
                        change = new Intent(NutritionActivity.this, AccountActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.exercises:
                        change = new Intent(NutritionActivity.this, ExercisesActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.workouts:
                        change = new Intent(NutritionActivity.this, WorkoutsActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.foods:
                        change = new Intent(NutritionActivity.this, NutritionActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.diets:
                        change = new Intent(NutritionActivity.this, DietPlansActivity.class);
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
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        String itemid = snapshot.getId();
        Log.d("ITEM_CLICK", "ID IS:  " + itemid);
        Intent i = new Intent(NutritionActivity.this, FoodProfile.class);
        i.putExtra("id",itemid);
        String foodcategory = getIntent().getStringExtra("foodcategory");
        if (foodcategory==null){
            foodcategory = "nothing";
        }
        i.putExtra("foodcategory", foodcategory);
        startActivity(i);
    }
    @Override
    public boolean onSupportNavigateUp() {
        change = new Intent(NutritionActivity.this, DietPlansActivity.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(change);
        overridePendingTransition(0, 0);
        return true;
    }

    @Override
    public void onBackPressed() {
        String foodcategory = getIntent().getStringExtra("foodcategory");
        if (foodcategory.contains("breakfast") || foodcategory.contains("lunch") || foodcategory.contains("dinner") || foodcategory.contains("snack"))
        {
            change = new Intent(NutritionActivity.this, DietPlansActivity.class);
            change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(change);
            overridePendingTransition(0, 0);
        }
        else
        {
            finish();
        }
    }
}