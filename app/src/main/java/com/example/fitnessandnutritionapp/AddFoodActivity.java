package com.example.fitnessandnutritionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFoodActivity extends AppCompatActivity {
    Intent change;
    EditText foodname, servingSize, protein, carbs, fat, sugars, fiber, saturated, cholesterol, sodium, potassium, calcium, iron, vitamind;
    Button addFood;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        foodname = findViewById(R.id.foodName);
        protein = findViewById(R.id.foodProtein);
        carbs = findViewById(R.id.foodCarbs);
        fat = findViewById(R.id.foodFat);
        sugars = findViewById(R.id.foodSugars);
        fiber = findViewById(R.id.foodFiber);
        saturated = findViewById(R.id.foodSaturatedFats);
        cholesterol = findViewById(R.id.foodCholesterol);
        sodium = findViewById(R.id.foodSodium);
        potassium = findViewById(R.id.foodPotassium);
        calcium = findViewById(R.id.foodCalcium);
        iron = findViewById(R.id.foodIron);
        vitamind = findViewById(R.id.foodVitaminD);
        servingSize = findViewById(R.id.foodServingSize);
        addFood = findViewById(R.id.addFood);
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((foodname.getText().toString().matches(""))||(protein.getText().toString().matches(""))||
                        (carbs.getText().toString().matches(""))||(fat.getText().toString().matches(""))||(servingSize.getText().toString().matches(""))){
                    Toast.makeText(AddFoodActivity.this, "Mandatory field with asterisk (*) must be filled",Toast.LENGTH_SHORT).show();
                }
                else {
                    final DocumentReference docRef = db.collection("NextFood").document("1");
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                final DocumentSnapshot nextFood = task.getResult();
                                if (nextFood != null) {
                                    String doc = (String) nextFood.get("nextID");
                                    final DocumentReference documentReference = db.collection("Foods").document(doc);
                                    double kcal = Double.parseDouble(protein.getText().toString())*4 + Double.parseDouble(fat.getText().toString())*9 + Double.parseDouble(carbs.getText().toString())*4;
                                    if (sugars.getText().toString().matches("")){
                                        sugars.setText("0");
                                    }
                                    if (calcium.getText().toString().matches("")){
                                        calcium.setText("0");
                                    }
                                    if (cholesterol.getText().toString().matches("")){
                                        cholesterol.setText("0");
                                    }
                                    if (fiber.getText().toString().matches("")){
                                        fiber.setText("0");
                                    }
                                    if (iron.getText().toString().matches("")){
                                        iron.setText("0");
                                    }
                                    if (potassium.getText().toString().matches("")){
                                        potassium.setText("0");
                                    }
                                    if (saturated.getText().toString().matches("")){
                                        saturated.setText("0");
                                    }
                                    if (sodium.getText().toString().matches("")){
                                        sodium.setText("0");
                                    }
                                    if (sugars.getText().toString().matches("")){
                                        sugars.setText("0");
                                    }
                                    if (vitamind.getText().toString().matches("")){
                                        vitamind.setText("0");
                                    }
                                    String[] keyw = foodname.getText().toString().toLowerCase().split(" ");
                                    List<String> keywords = new ArrayList<String>();
                                    String temp = "";
                                    for (int i = 0; i< keyw.length; i++){
                                        keywords.add(keyw[i]);
                                        temp = keyw[i];
                                        for (int k = i+1; k<keyw.length; k++) {
                                            temp = temp + " " + keyw[k];
                                            keywords.add(temp);
                                        }
                                    }
                                    final Map<String,Object> foodData = new HashMap<>();
                                    foodData.put("Added Sugar (g)",sugars.getText().toString());
                                    foodData.put("Calcium (mg)",calcium.getText().toString());
                                    foodData.put("Calories",kcal+"");
                                    foodData.put("Carbohydrate (g)",carbs.getText().toString());
                                    foodData.put("Cholesterol (mg)",cholesterol.getText().toString());
                                    foodData.put("Fat (g)",fat.getText().toString());
                                    foodData.put("Fiber (g)",fiber.getText().toString());
                                    foodData.put("ID","A"+doc);
                                    foodData.put("Iron, Fe (mg)",iron.getText().toString());
                                    foodData.put("Potassium, K (mg)",potassium.getText().toString());
                                    foodData.put("Protein (g)",protein.getText().toString());
                                    foodData.put("Saturated Fats (g)",saturated.getText().toString());
                                    foodData.put("Serving Weight 1 (g)",servingSize.getText().toString());
                                    foodData.put("Sodium (mg)",sodium.getText().toString());
                                    foodData.put("Sugars (g)",sugars.getText().toString());
                                    foodData.put("Vitamin D (mcg)",vitamind.getText().toString());
                                    foodData.put("keywords", keywords);
                                    foodData.put("name",foodname.getText().toString());
                                    foodData.put("names lowercase",foodname.getText().toString().toLowerCase());

                                    DocumentReference userRef = db.collection("Users").document(userID);


                                    documentReference.set(foodData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("ONSUCCESS: ", "Food created");
                                        }
                                    });
                                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                final DocumentSnapshot userData = task.getResult();
                                                if (userData != null) {
                                                    documentReference.update("verified", userData.get("permission-level").toString());
                                                }
                                            }
                                        }
                                    });
                                    docRef.update("nextID",Integer.parseInt(doc) + 1+"");
                                    Intent i = new Intent(AddFoodActivity.this, NutritionActivity.class);
                                    String foodcategory = getIntent().getStringExtra("foodcategory");
                                    if (foodcategory==null){
                                        foodcategory = "nothing";
                                    }
                                    i.putExtra("foodcategory", foodcategory);
                                    startActivity(i);
                                }
                            }
                        }
                    });
                }
            }
        });


        getSupportActionBar().setTitle("CREATE A FOOD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String foodcategory = getIntent().getStringExtra("foodcategory");
    }
    @Override
    public boolean onSupportNavigateUp() {
        final String foodcategory = getIntent().getStringExtra("foodcategory");
        change = new Intent(AddFoodActivity.this, NutritionActivity.class);
        change.putExtra("foodcategory",foodcategory);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(change);
        overridePendingTransition(0, 0);
        return true;
    }

    @Override
    public void onBackPressed() {
        final String foodcategory = getIntent().getStringExtra("foodcategory");
        change = new Intent(AddFoodActivity.this, NutritionActivity.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(change);
        overridePendingTransition(0, 0);
    }
}