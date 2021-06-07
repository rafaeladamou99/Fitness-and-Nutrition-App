package com.example.fitnessandnutritionapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class FoodProfile extends AppCompatActivity {

    CardView expandable;
    TextView showhide;
    TextView carbs, fat, protein, sugars, fiber, saturated, cholesterol, sodium, potassium, calcium, iron, vitamind, kcal, addFood, verified;
    EditText serving, servingsize;
    ImageView imageView;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    BottomNavigationView bottomBar;
    Intent change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fAuth = FirebaseAuth.getInstance();
        final String userID = fAuth.getCurrentUser().getUid();
        bottomBar = findViewById(R.id.bottomBar);
        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.kcals);
        carbs = findViewById(R.id.carbs);
        fat = findViewById(R.id.fat);
        protein = findViewById(R.id.protein);
        expandable = findViewById(R.id.expandableView);
        showhide = findViewById(R.id.showmore);
        sugars = findViewById(R.id.sugars);
        fiber = findViewById(R.id.fiber);
        saturated = findViewById(R.id.saturated);
        cholesterol = findViewById(R.id.cholesterol);
        sodium = findViewById(R.id.sodium);
        potassium = findViewById(R.id.potassium);
        calcium = findViewById(R.id.calcium);
        iron = findViewById(R.id.iron);
        vitamind = findViewById(R.id.vitamind);
        serving = findViewById(R.id.serving);
        servingsize = findViewById(R.id.servingsize);
        kcal = findViewById(R.id.kcal);
        verified = findViewById(R.id.verified);
        addFood = findViewById(R.id.addFood);
        db = FirebaseFirestore.getInstance();
        final String id = getIntent().getStringExtra("id");
        final String foodcategory = getIntent().getStringExtra("foodcategory");
        if (foodcategory.contains("nothing")){
        }
        else{
            addFood.setVisibility(View.VISIBLE);
        }
        DocumentReference docRef = db.collection("Foods").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    final DocumentSnapshot foodData1 = task.getResult();
                    if (foodData1!=null){
                        float sizeWritten = Float.parseFloat(foodData1.getString("Serving Weight 1 (g)"));
                        servingsize.setText(String.valueOf(sizeWritten));
                        serving.setText("1");
                        getSupportActionBar().setTitle(foodData1.getString("name"));
                    }
                }
            }
        });
        fillData(id);

        serving.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                fillData(id);
            }
        });

        servingsize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                fillData(id);
            }
        });

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = db.collection("Users").document(userID);
                String idfixed = id;
                while (idfixed.length()<4){
                    idfixed = "0" + idfixed;
                }
                String add = getSupportActionBar().getTitle().toString() +"%A" + idfixed + "HOWMUCH" + kcal.getText()+ "TS" + System.currentTimeMillis();
                if (foodcategory.contains("breakfast")){
                    docRef.update("breakfast", FieldValue.arrayUnion(add));
                }
                if (foodcategory.contains("lunch")){
                    docRef.update("lunch", FieldValue.arrayUnion(add));
                }
                if (foodcategory.contains("dinner")){
                    docRef.update("dinner", FieldValue.arrayUnion(add));
                }
                if (foodcategory.contains("snack")){
                    docRef.update("snack", FieldValue.arrayUnion(add));
                }
                Intent change = new Intent(FoodProfile.this,DietPlansActivity.class);
                change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(change);

            }
        });


        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:
                        change = new Intent(FoodProfile.this, AccountActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.exercises:
                        change = new Intent(FoodProfile.this, ExercisesActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.workouts:
                        change = new Intent(FoodProfile.this, WorkoutsActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.foods:
                        change = new Intent(FoodProfile.this, NutritionActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.diets:
                        change = new Intent(FoodProfile.this, DietPlansActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }


    public void showmore(View view) {

        if (expandable.getVisibility() == View.GONE){
            showhide.setText("Show Less");
            TransitionManager.beginDelayedTransition(expandable, new AutoTransition());
            expandable.setVisibility(View.VISIBLE);
        } else {
            showhide.setText("Show More");
            TransitionManager.beginDelayedTransition(expandable, new AutoTransition());
            expandable.setVisibility(View.GONE);
        }

    }

    public void fillData(String id){
        final DecimalFormat df = new DecimalFormat("#.#");
        DocumentReference docRef = db.collection("Foods").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    final DocumentSnapshot foodData = task.getResult();
                    if (foodData!=null){
                        float proteinValue = Float.parseFloat(foodData.getString("Protein (g)"));
                        float carbsValue = Float.parseFloat(foodData.getString("Carbohydrate (g)"));
                        float fatValue = Float.parseFloat(foodData.getString("Fat (g)"));
                        float sugarsValue = Float.parseFloat(foodData.getString("Sugars (g)"));
                        float fiberValue = Float.parseFloat(foodData.getString("Fiber (g)"));
                        float saturatedValue = Float.parseFloat(foodData.getString("Saturated Fats (g)"));
                        float cholesterolValue = Float.parseFloat(foodData.getString("Cholesterol (mg)"));
                        float sodiumValue = Float.parseFloat(foodData.getString("Sodium (mg)"));
                        float potassiumValue = Float.parseFloat(foodData.getString("Potassium, K (mg)"));
                        float calciumValue = Float.parseFloat(foodData.getString("Calcium (mg)"));
                        float ironValue = Float.parseFloat(foodData.getString("Iron, Fe (mg)"));
                        float vitamindValue = Float.parseFloat(foodData.getString("Vitamin D (mcg)"));
                        float calories = Float.parseFloat(foodData.getString("Calories"));
                        int verification = Integer.parseInt(foodData.getString("verified"));

                        float sizeWritten = Float.parseFloat(foodData.getString("Serving Weight 1 (g)"));
                        float newsize, servings;
                        if (servingsize.getText().toString().matches("")){
                            newsize=0;
                        }
                        else {
                            newsize = Float.parseFloat(String.valueOf(servingsize.getText()));
                        }
                        if (serving.getText().toString().matches("")){
                            servings=0;
                        }
                        else {
                            servings = Float.parseFloat(String.valueOf(serving.getText()));
                        }


                        float sizemult = ((float)newsize / (float)sizeWritten) * servings;
                        if  (verification==1){
                            verified.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                        protein.setText(df.format(proteinValue * sizemult).replaceAll(",",".") + "g");
                        sugars.setText(df.format(sugarsValue * sizemult).replaceAll(",",".") + "(g)");
                        carbs.setText(df.format(carbsValue * sizemult).replaceAll(",",".") + "g");
                        fat.setText(df.format(fatValue * sizemult).replaceAll(",",".") + "g");
                        fiber.setText(df.format(fiberValue * sizemult).replaceAll(",",".") + "(g)");
                        saturated.setText(df.format(saturatedValue * sizemult).replaceAll(",",".") + "(g)");
                        cholesterol.setText(df.format(cholesterolValue * sizemult).replaceAll(",",".") + "(mg)");
                        sodium.setText(df.format(sodiumValue * sizemult).replaceAll(",",".") + "(mg)");
                        potassium.setText(df.format(potassiumValue * sizemult).replaceAll(",",".") + "(mg)");
                        calcium.setText(df.format(calciumValue * sizemult).replaceAll(",",".") + "(mg)");
                        iron.setText(df.format(ironValue * sizemult).replaceAll(",",".") + "(mg)");
                        vitamind.setText(df.format(vitamindValue * sizemult).replaceAll(",",".") + "(mcg)");
                        kcal.setText(df.format(calories * sizemult).replaceAll(",","."));
                        Log.d("Protein: 1111   ",proteinValue + "");
                    }
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
            change = new Intent(FoodProfile.this, NutritionActivity.class);
            final String foodcategory = getIntent().getStringExtra("foodcategory");
            change.putExtra("foodcategory",foodcategory);
            change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(change);
            overridePendingTransition(0, 0);
        return true;
    }

    @Override
    public void onBackPressed() {
            change = new Intent(FoodProfile.this, NutritionActivity.class);
            final String foodcategory = getIntent().getStringExtra("foodcategory");
            change.putExtra("foodcategory",foodcategory);
            change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(change);
            overridePendingTransition(0, 0);
    }

}