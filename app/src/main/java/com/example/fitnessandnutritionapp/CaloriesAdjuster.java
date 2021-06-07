package com.example.fitnessandnutritionapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CaloriesAdjuster extends AppCompatActivity implements CaloriesHelp.CaloriesHelpListener{
    private SeekBar seekBar;
    private TextView kcals,whidden,help;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private NumberPicker proteinadjuster, fatadjuster, carbsadjuster;
    String userID;
    private Button cancel, update;
    PieChart pieChart;
    Intent change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories_adjuster);
        getSupportActionBar().setTitle("CALORIES ADJUSTING");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        seekBar = findViewById(R.id.seekBar);
        kcals = findViewById(R.id.kcals);
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        proteinadjuster = findViewById(R.id.proteinadjuster);
        fatadjuster = findViewById(R.id.fatadjuster);
        carbsadjuster = findViewById(R.id.carbsadjuster);
        whidden = findViewById(R.id.whidden);
        pieChart = findViewById(R.id.pie);
        cancel = findViewById(R.id.cancel);
        update = findViewById(R.id.update);
        help = findViewById(R.id.helplink);


        DocumentReference docRef = db.collection("Users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    final DocumentSnapshot userData = task.getResult();
                    int kcal = Integer.parseInt(userData.get("targetkcal").toString());
                    if (kcal==0){
                        kcal = 2000;
                    }
                    kcals.setText(kcal+"\nkcal");
                    proteinadjuster.setMinValue(0);
                    proteinadjuster.setMaxValue(999);
                    proteinadjuster.setValue(Integer.parseInt(userData.get("protein").toString()));
                    carbsadjuster.setMinValue(0);
                    carbsadjuster.setMaxValue(2000);
                    carbsadjuster.setValue(Integer.parseInt(userData.get("carbs").toString()));
                    fatadjuster.setMinValue(0);
                    fatadjuster.setMaxValue(999);
                    fatadjuster.setValue(Integer.parseInt(userData.get("fat").toString()));
                    ArrayList<String> weights = (ArrayList<String>) userData.get("weight");
                    String[] weight = weights.get(weights.size()-1).split("time");
                    whidden.setText(weight[0]);
                    pieMethod();
                }
            }
        });

        proteinadjuster.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                int total = newValue * 4 + carbsadjuster.getValue() * 4 + fatadjuster.getValue() * 9;
                kcals.setText(total+"\nkcal");
                pieMethod();
            }
        });
        carbsadjuster.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                int total = newValue * 4 + proteinadjuster.getValue() * 4 + fatadjuster.getValue() * 9;
                kcals.setText(total+"\nkcal");
                pieMethod();
            }
        });
        fatadjuster.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                int total = newValue * 9 + carbsadjuster.getValue() * 4 + proteinadjuster.getValue() * 4;
                kcals.setText(total+"\nkcal");
                pieMethod();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int cals, boolean b) {
                kcals.setText((1000+cals*50) + "\nkcal");
                double kg = Double.parseDouble(whidden.getText().toString());
                int p = (int)kg*2;
                int f = (int)kg;
                int carbs = ((1000+cals*50)-((int)(kg*2*4)+(int)(kg*9)))/4;
                if (carbs<0) {
                    carbsadjuster.setValue(0);
                    int x = 0;
                    while (((1000+cals * 50) - (p * 4 + f * 9)) < 0) {
                        if (x==0){
                            p = p - 1;
                            x=1;
                        }
                        else{
                            f = f - 1;
                            x=0;
                        }

                    }
                }
                else{
                    carbsadjuster.setValue(carbs);
                }
                proteinadjuster.setValue(p);
                fatadjuster.setValue(f);
                pieMethod();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change = new Intent(CaloriesAdjuster.this, Goals.class);
                change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(change);
                overridePendingTransition(0,0);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = db.collection("Users").document(userID);
                String kcalvalue = kcals.getText().toString();
                String[] kcalsplit = kcalvalue.split("\n");
                docRef.update("targetkcal", kcalsplit[0]);
                docRef.update("protein", String.valueOf(proteinadjuster.getValue()));
                docRef.update("carbs", String.valueOf(carbsadjuster.getValue()));
                docRef.update("fat", String.valueOf(fatadjuster.getValue()));

                change = new Intent(CaloriesAdjuster.this, Goals.class);
                change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(change);
                overridePendingTransition(0,0);
            }
        });


    }

    private void pieMethod() {
        ArrayList<PieEntry> macros = new ArrayList<>();
        int p = proteinadjuster.getValue();
        int f = fatadjuster.getValue();
        int c = carbsadjuster.getValue();

        macros.add(new PieEntry(p*4,"protein"));
        macros.add(new PieEntry(f*9,"fat"));
        macros.add(new PieEntry(c*4,"carbs"));
        PieDataSet pieDataSet = new PieDataSet(macros,"");

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Calories\nfrom\nMacros");
        pieChart.animate();
    }

    public void showDialog(View view) {
        CaloriesHelp caloriesHelp = new CaloriesHelp();
        caloriesHelp.show(getSupportFragmentManager(),"Dialog");
    }

    @Override
    public void applyTexts(String calories1, String protein1, String fat1, String carbs1) {
        kcals.setText(calories1);
        int protein = Integer.parseInt(protein1.replaceAll("g",""));
        int fat = Integer.parseInt(fat1.replaceAll("g",""));
        int carbs = Integer.parseInt(carbs1.replaceAll("g",""));
        proteinadjuster.setValue(protein);
        proteinadjuster.setMinValue(0);
        proteinadjuster.setMaxValue(999);

        carbsadjuster.setValue(carbs);
        carbsadjuster.setMinValue(0);
        carbsadjuster.setMaxValue(2000);

        fatadjuster.setValue(fat);
        fatadjuster.setMinValue(0);
        fatadjuster.setMaxValue(999);
        pieMethod();
    }
    @Override
    public boolean onSupportNavigateUp() {
            change = new Intent(CaloriesAdjuster.this, Goals.class);
            change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(change);
            overridePendingTransition(0, 0);
        return true;
    }

    @Override
    public void onBackPressed() {
            change = new Intent(CaloriesAdjuster.this, Goals.class);
            change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(change);
            overridePendingTransition(0, 0);
    }
}