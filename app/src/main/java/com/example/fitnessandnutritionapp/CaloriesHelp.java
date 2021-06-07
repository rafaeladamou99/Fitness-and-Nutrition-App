package com.example.fitnessandnutritionapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CaloriesHelp extends DialogFragment implements AdapterView.OnItemSelectedListener {
    Spinner spinneractivity, spinnergoal;
    Button calculate;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    String userID;
    TextView g, h, w, a, prot, carbs, fat, cal;
    private CaloriesHelpListener caloriesHelpListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        DocumentReference docRef = db.collection("Users").document(userID);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.calories_help_layout,null);
        a = view.findViewById(R.id.ageretrieved);
        w = view.findViewById(R.id.weightretrieved);
        h = view.findViewById(R.id.heightretrieved);
        g = view.findViewById(R.id.genderetrieved);
        cal = view.findViewById(R.id.caloriesgen);
        prot = view.findViewById(R.id.proteingennum);
        fat = view.findViewById(R.id.fatgennum);
        carbs = view.findViewById(R.id.carbsgennum);
        calculate = view.findViewById(R.id.calculate);
        spinneractivity = view.findViewById(R.id.activitylevelspinner);
        ArrayAdapter<String> adapteractivity = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.activitylevel));
        adapteractivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneractivity.setAdapter(adapteractivity);

        spinnergoal = view.findViewById(R.id.goalspinner);
        ArrayAdapter<String> adaptergoal = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.goal));
        adaptergoal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnergoal.setAdapter(adaptergoal);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    final DocumentSnapshot userData = task.getResult();
                    int height = Integer.parseInt(userData.get("height").toString());
                    h.setText(String.valueOf(height));
                    int age = Integer.parseInt(userData.get("age").toString());
                    a.setText(String.valueOf(age));
                    String gender = userData.get("gender").toString();
                    g.setText(gender);
                    ArrayList<String> weights = (ArrayList<String>) userData.get("weight");
                    w.setText(weights.get(weights.size()-1));
                }
            }
        });


        final double maintenance = 0;

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double bmr = 0;
                String[] wsplit = w.getText().toString().split("time");
                double weight = Double.parseDouble(wsplit[0]);
                int age = Integer.parseInt((String) a.getText());
                int height = Integer.parseInt((String) h.getText());
                if (g.getText().toString().contains("male")){
                    bmr = 10 * weight + 6.25 * height - 5 * age + 5;
                }
                else{
                    bmr = 10 * weight + 6.25 * height - 5 * age - 161;
                }
                String activitylvl = spinneractivity.getSelectedItem().toString();
                String goalset = spinnergoal.getSelectedItem().toString();
                double alvl = 0;
                double cal0 = 0;
                if (activitylvl.contains("Sedentary ")){
                    alvl = 1.2;
                }
                else if (activitylvl.contains("Lightly active")){
                    alvl = 1.375;
                }
                else if (activitylvl.contains("Moderately active")){
                    alvl = 1.55;
                }
                else if (activitylvl.contains("Very active")){
                    alvl = 1.725;
                }
                else{
                    alvl = 1.9;
                }

                if (goalset.contains("Loss")){
                    cal0 = bmr * alvl * 0.8;
                }
                else if (goalset.contains("Gain")){
                    cal0 = bmr * alvl + 500;
                }
                else{
                    cal0 = bmr * alvl;
                }

                double protein0 = weight*2;
                double fat0 = weight;
                double carbs0 = (cal0 - protein0 * 4 - fat0 * 9)/4;
                cal.setText(String.valueOf((int)cal0));
                prot.setText(String.valueOf((int)protein0));;
                fat.setText(String.valueOf((int)fat0));;
                carbs.setText(String.valueOf((int)carbs0));;


            }
        });






                builder.setView(view)
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            String protein1 = prot.getText().toString();
                            String calories1 = cal.getText().toString();
                            String fat1 = fat.getText().toString();
                            String carbs1 = carbs.getText().toString();
                            caloriesHelpListener.applyTexts(calories1,protein1,fat1,carbs1);
                            }
                        });

        return builder.create();

    }

    public interface CaloriesHelpListener{
        void applyTexts(String calories1, String protein1, String fat1, String carbs1);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            caloriesHelpListener = (CaloriesHelpListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ExampleDialogListener");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
