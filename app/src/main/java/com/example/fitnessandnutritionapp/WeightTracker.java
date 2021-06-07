package com.example.fitnessandnutritionapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeightTracker extends AppCompatActivity {
    BottomNavigationView bottomBar;
    Intent change;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    String userID;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
    TextView w,currentweighttime;
    Button addWeight;
    LineChart graph2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_tracker);
        getSupportActionBar().setTitle("WEIGHT TRACKER");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bottomBar = findViewById(R.id.bottomBar);
        currentweighttime = findViewById(R.id.currentweighttime);
        w = findViewById(R.id.currentweight);
        bottomBar.setSelectedItemId(R.id.account);
        graph2 = findViewById(R.id.graph);
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        addWeight = findViewById(R.id.addnewweight);

        DocumentReference docRef = db.collection("Users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    final DocumentSnapshot userData = task.getResult();
                    if (userData!=null){
                        ArrayList<String> weights = (ArrayList<String>) userData.get("weight");//we got the list of weights & timestamps
                        double minweight = 99999;
                        double maxweight = -10;

                        for (int j = 0; j<(weights.size()-1); j++){
                            for (int k = j + 1; k<weights.size(); k++){
                                String[] str1 = weights.get(j).split("time");
                                String[] str2 = weights.get(k).split("time");
                                long x1 = Long.parseLong(str1[1]);
                                double x01 = Double.parseDouble(str1[0]);
                                long x2 = Long.parseLong(str2[1]);
                                double x02 = Double.parseDouble(str2[0]);
                                if (x01<minweight){
                                    minweight = x01;
                                }
                                if (x02<minweight){
                                    minweight = x02;
                                }
                                if (x01>maxweight){
                                    maxweight = x01;
                                }
                                if (x02>maxweight){
                                    maxweight = x02;
                                }
                                if (x2<x1){
                                    String temp = weights.get(j);
                                    weights.set(j, weights.get(k));
                                    weights.set(k,temp);
                                }
                            }
                        }
                        ArrayList<Entry> dataSet = new ArrayList<Entry>();
                        ArrayList<Long> dates = new ArrayList<>();
                        for (int i = 0; i<weights.size(); i++){
                            String[] arrOfStr = weights.get(i).split("time");
                            double y = Double.parseDouble(arrOfStr[0]);
                            long x = Long.parseLong(arrOfStr[1]);
                            dates.add(x);
                            dataSet.add(new Entry((float)x, (float) y));
                        }
                        LineDataSet lineDataSet = new LineDataSet(dataSet,"weights");
                        lineDataSet.setColor(Color.GREEN);
                        lineDataSet.setCircleRadius(5);
                        lineDataSet.setCircleColor(Color.parseColor("#000000"));
                        lineDataSet.setValueTextSize(10);
                        lineDataSet.setValueTextColor(Color.BLACK);
                        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
                        iLineDataSets.add(lineDataSet);
                        LineData lineData = new LineData(iLineDataSets);
                        graph2.setData(lineData);

                        YAxis lyAxis = graph2.getAxisLeft();
                        YAxis ryAxis = graph2.getAxisRight();
                        ryAxis.setEnabled(false);
                        XAxis xAxis = graph2.getXAxis();
                        xAxis.setLabelCount(dates.size(), true);
                        xAxis.setValueFormatter(new ValueFormatter() {
                                                    @Override
                                                    public String getAxisLabel(float value, AxisBase axis) {
                                                        return sdf.format(new Date((long)value));
                                                    }
                                                });
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawLabels(true);
                        xAxis.setTextSize(12);
                        lyAxis.setTextSize(12);
                        ryAxis.setTextSize(12);
                        xAxis.setDrawAxisLine(true);
                        xAxis.setDrawGridLines(true);
                        xAxis.setGranularity(100f);
                        graph2.setScaleMinima((float) dates.size() / 5f, 1f);
                        if (dates.size()<5){

                        }
                        else{
                            graph2.moveViewToX(dates.get(dates.size()-5));
                        }

                        graph2.getXAxis().setGranularity(10f);
                        graph2.getXAxis().setGranularityEnabled(true);

                        graph2.invalidate();

                        String[] min = weights.get(0).split("time");
                        String[] max = weights.get(weights.size()-1).split("time");
                        w.setText(max[0] + "kg");
                        currentweighttime.setText(max[1]);



                    }
                }
            }
        });

        addWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change = new Intent(WeightTracker.this, AddWeight.class);
                change.putExtra("CurrentWeight",Double.parseDouble(String.valueOf(w.getText()).substring(0,(String.valueOf(w.getText()).length()) - 2)));
                change.putExtra("Time",Long.parseLong((String) currentweighttime.getText()));
                change.putExtra("Add","Nothing");
                startActivity(change);
                overridePendingTransition(0,0);
            }
        });


        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:
                        change = new Intent(WeightTracker.this, AccountActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.exercises:
                        change = new Intent(WeightTracker.this, ExercisesActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.workouts:
                        change = new Intent(WeightTracker.this, WorkoutsActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.foods:
                        change = new Intent(WeightTracker.this, NutritionActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.diets:
                        change = new Intent(WeightTracker.this, DietPlansActivity.class);
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
        change = new Intent(WeightTracker.this, AccountActivity.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(change);
        overridePendingTransition(0,0);
        return true;
    }

    @Override
    public void onBackPressed() {
        change = new Intent(WeightTracker.this, AccountActivity.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(change);
        overridePendingTransition(0,0);
    }

    private class MyValueFormatter extends ValueFormatter{

    }


}