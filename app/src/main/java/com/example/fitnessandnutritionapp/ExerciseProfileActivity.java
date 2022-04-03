package com.example.fitnessandnutritionapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExerciseProfileActivity extends YouTubeBaseActivity {

    BottomNavigationView bottomBar;
    Intent change;
    private FirebaseFirestore db;
    TextView exerciseDescription, exerciseName;

    YouTubePlayerView mYoutubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_profile);
        exerciseName = findViewById(R.id.exercisename);

        bottomBar = findViewById(R.id.bottomBar);
        exerciseDescription = findViewById(R.id.exercisedescription);

        mYoutubePlayerView = (YouTubePlayerView) findViewById(R.id.YoutubePlay);



        bottomBar.setSelectedItemId(R.id.exercises);
        int exercise1 = getIntent().getIntExtra("keyexercise",1);
        final String exercise = String.valueOf(exercise1);
        String col = getIntent().getStringExtra("musclesubgroup");
        db = FirebaseFirestore.getInstance();
        DocumentReference exerciseRef = db.collection(col).document(exercise);
        exerciseRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    final DocumentSnapshot exerciseData = task.getResult();
                    if (exerciseData != null){
                        exerciseName.setText(exerciseData.getString("name"));
                        exerciseDescription.setText(exerciseData.getString("description"));
                        //Picasso.get().load(exerciseData.getString("URL")).into(exerciseImage);
                        //YOUTUBE VIDEO
                        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                    String video = exerciseData.getString("vURL");
                                    youTubePlayer.loadVideo(video);
                                }
                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                                Log.d("LOGGER", "FAILED");
                            }
                        };
                        mYoutubePlayerView.initialize(YoutubeConfig.getApiKey(), mOnInitializedListener);

                    } else {
                        Log.d("LOGGER", "No such exercise");
                    }
                } else {
                    Log.d("LOGGER", "No such exercise", task.getException());
                }
            }
        });


        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:
                        change = new Intent(ExerciseProfileActivity.this, AccountActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.exercises:
                        change = new Intent(ExerciseProfileActivity.this, ExercisesActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.workouts:
                        change = new Intent(ExerciseProfileActivity.this, WorkoutsActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.foods:
                        change = new Intent(ExerciseProfileActivity.this, NutritionActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.diets:
                        change = new Intent(ExerciseProfileActivity.this, DietPlansActivity.class);
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
    public boolean onNavigateUp() {
        int musclegroup = getIntent().getIntExtra("keymusclegroup",1);
        int musclesubgroup = getIntent().getIntExtra("keymusclesubgroup",1);
        change = new Intent(ExerciseProfileActivity.this, MuscleGroupSubExercises.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        change.putExtra("keymusclegroup",musclegroup);
        change.putExtra("keymusclesubgroup",musclesubgroup);
        startActivity(change);
        overridePendingTransition(0, 0);
        return true;
    }


    public void goBack(View view) {
        int musclegroup = getIntent().getIntExtra("keymusclegroup",1);
        int musclesubgroup = getIntent().getIntExtra("keymusclesubgroup",1);
        change = new Intent(ExerciseProfileActivity.this, MuscleGroupSubExercises.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        change.putExtra("keymusclegroup",musclegroup);
        change.putExtra("keymusclesubgroup",musclesubgroup);
        startActivity(change);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        int musclegroup = getIntent().getIntExtra("keymusclegroup",1);
        int musclesubgroup = getIntent().getIntExtra("keymusclesubgroup",1);
        change = new Intent(ExerciseProfileActivity.this, MuscleGroupSubExercises.class);
        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        change.putExtra("keymusclegroup",musclegroup);
        change.putExtra("keymusclesubgroup",musclesubgroup);
        startActivity(change);
        overridePendingTransition(0, 0);
    }
}