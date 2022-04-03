package com.example.fitnessandnutritionapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class DietPlansActivity extends AppCompatActivity implements FirestoreAdapterLogged.OnListItemClick {

    BottomNavigationView bottomBar;
    Intent change;
    private RecyclerView breakfastFoods, lunchFoods, dinnerFoods, snackFoods;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private TextView breakfastkcal, lunchkcal, dinnerkcal, snackkcal, goal, food, remaining, remainingcol;
    private TextView addbreakfast, adddinner, addlunch, addsnack;
    private FirestoreAdapterLogged adapter1, adapter2, adapter3, adapter4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietplans);getSupportActionBar().setTitle("FOOD LOGGER");
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.diets);
        breakfastFoods = findViewById(R.id.breakfastrecycler);
        lunchFoods = findViewById(R.id.lunchrecycler);
        dinnerFoods = findViewById(R.id.dinnerrecycler);
        snackFoods = findViewById(R.id.snackrecycler);
        breakfastkcal = findViewById(R.id.breakfastkcal);
        lunchkcal = findViewById(R.id.lunchkcal);
        dinnerkcal = findViewById(R.id.dinnerkcal);
        snackkcal = findViewById(R.id.snackkcal);
        goal = findViewById(R.id.Goal);
        food = findViewById(R.id.Food);
        remaining = findViewById(R.id.remaining);
        remainingcol = findViewById(R.id.remaining0);
        addbreakfast = findViewById(R.id.addBreakfast);
        addlunch = findViewById(R.id.addLunch);
        adddinner = findViewById(R.id.addDinner);
        addsnack = findViewById(R.id.addSnack);
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        String UserID = fAuth.getCurrentUser().getUid();

        Query query = db.collection("Empty");
        Log.d("YOUUUUUUUU", UserID);
        Log.d("YOUUUUUUUU", UserID);
        Log.d("YOUUUUUUUU", UserID);
        Log.d("YOUUUUUUUU", UserID);
        Log.d("YOUUUUUUUU", UserID);
        Log.d("YOUUUUUUUU", UserID);
        Log.d("YOUUUUUUUU", UserID);



        final PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();

        final FirestorePagingOptions<FoodsLoggedModel> options = new FirestorePagingOptions.Builder<FoodsLoggedModel>()
                .setQuery(query, config, FoodsLoggedModel.class)
                .build();

        ArrayList<String> arrayList = new ArrayList<String>();

        adapter1 = new FirestoreAdapterLogged(options, this, arrayList, arrayList);
        adapter2 = new FirestoreAdapterLogged(options, this, arrayList, arrayList);
        adapter3 = new FirestoreAdapterLogged(options, this, arrayList, arrayList);
        adapter4 = new FirestoreAdapterLogged(options, this, arrayList, arrayList);




        addsnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change = new Intent(DietPlansActivity.this, NutritionActivity.class);
                change.putExtra("foodcategory","snack");
                startActivity(change);
                overridePendingTransition(0,0);
            }
        });

        adddinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change = new Intent(DietPlansActivity.this, NutritionActivity.class);
                change.putExtra("foodcategory","dinner");
                startActivity(change);
                overridePendingTransition(0,0);
            }
        });

        addlunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change = new Intent(DietPlansActivity.this, NutritionActivity.class);
                change.putExtra("foodcategory","lunch");
                startActivity(change);
                overridePendingTransition(0,0);
            }
        });

        addbreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change = new Intent(DietPlansActivity.this, NutritionActivity.class);
                change.putExtra("foodcategory","breakfast");
                startActivity(change);
                overridePendingTransition(0,0);
            }
        });

        final DocumentReference docRef = db.collection("Users").document(UserID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot foodLogged = task.getResult();
                    ArrayList<String> breakfast = (ArrayList<String>) foodLogged.get("breakfast");
                    int modbreakfast = 0;
                    int bSize = (breakfast == null) ? 0 : breakfast.size();
                    for (int i = 0; i<bSize; i++){
                        String[] ts = breakfast.get(i).split("TS");
                        long timeobj = Long.parseLong(ts[1])/86400000;
                        long current = System.currentTimeMillis()/86400000;
                        if (current>timeobj){
                            breakfast.remove(i);
                            i=i-1;
                            modbreakfast = 1;
                        }
                    }
                    if (modbreakfast == 1){
                        docRef.update("breakfast",breakfast);
                    }

                    ArrayList<String> lunch = (ArrayList<String>) foodLogged.get("lunch");
                    int modlunch = 0;
                    int lSize = (lunch == null) ? 0 : lunch.size();
                    for (int i = 0; i<lSize; i++){
                        String[] ts = lunch.get(i).split("TS");
                        long timeobj = Long.parseLong(ts[1])/86400000;
                        long current = System.currentTimeMillis()/86400000;
                        if (current>timeobj){
                            lunch.remove(i);
                            i=i-1;
                            modlunch = 1;
                        }
                    }
                    if (modlunch == 1){
                        docRef.update("lunch",lunch);
                    }

                    ArrayList<String> dinner = (ArrayList<String>) foodLogged.get("dinner");
                    int moddinner = 0;
                    int dSize = (dinner == null) ? 0 : dinner.size();
                    for (int i = 0; i<dSize; i++){
                        String[] ts = dinner.get(i).split("TS");
                        long timeobj = Long.parseLong(ts[1])/86400000;
                        long current = System.currentTimeMillis()/86400000;
                        if (current>timeobj){
                            dinner.remove(i);
                            i=i-1;
                            moddinner = 1;
                        }
                    }
                    if (moddinner == 1){
                        docRef.update("dinner",dinner);
                    }

                    ArrayList<String> snack = (ArrayList<String>) foodLogged.get("snack");
                    int modsnack = 0;
                    int sSize = (snack == null) ? 0 : snack.size();
                    for (int i = 0; i<sSize; i++){
                        String[] ts = snack.get(i).split("TS");
                        long timeobj = Long.parseLong(ts[1])/86400000;
                        long current = System.currentTimeMillis()/86400000;
                        if (current>timeobj){
                            snack.remove(i);
                            i=i-1;
                            modsnack = 1;
                        }
                    }
                    if (modsnack == 1){
                        docRef.update("snack",snack);
                    }

                    ArrayList<String> bID = new ArrayList<String>();
                    ArrayList<String> lID = new ArrayList<String>();
                    ArrayList<String> dID = new ArrayList<String>();
                    ArrayList<String> sID = new ArrayList<String>();
                    ArrayList<String> bnames = new ArrayList<String>();
                    ArrayList<String> lnames = new ArrayList<String>();
                    ArrayList<String> dnames = new ArrayList<String>();
                    ArrayList<String> snames = new ArrayList<String>();
                    for (int i = 0; i<bSize;i++){
                        String[] tempbreakfast = breakfast.get(i).split("HOWMUCH");
                        String[] namesIds  = tempbreakfast[0].split("%");
                        bnames.add(namesIds[0]);
                        bID.add(namesIds[1]);
                    }
                    for (int i = 0; i<lSize;i++){
                        String[] templunch = lunch.get(i).split("HOWMUCH");
                        String[] namesIds  = templunch[0].split("%");
                        lnames.add(namesIds[0]);
                        lID.add(namesIds[1]);
                    }
                    for (int i = 0; i<dSize;i++){
                        String[] tempdinner = dinner.get(i).split("HOWMUCH");
                        String[] namesIds  = tempdinner[0].split("%");
                        dnames.add(namesIds[0]);
                        dID.add(namesIds[1]);
                    }
                    for (int i = 0; i<sSize;i++){
                        String[] tempsnack = snack.get(i).split("HOWMUCH");
                        String[] namesIds  = tempsnack[0].split("%");
                        snames.add(namesIds[0]);
                        sID.add(namesIds[1]);
                    }

                    FirestorePagingOptions<FoodsLoggedModel> options1 = options;
                    FirestorePagingOptions<FoodsLoggedModel> options2 = options;
                    FirestorePagingOptions<FoodsLoggedModel> options3 = options;
                    FirestorePagingOptions<FoodsLoggedModel> options4 = options;

                    if (!bID.isEmpty()){
                        Query query1 = db.collection("Foods")
                                .whereIn("ID", bID);
                        options1 = new FirestorePagingOptions.Builder<FoodsLoggedModel>()
                                .setLifecycleOwner(DietPlansActivity.this)
                                .setQuery(query1, config,FoodsLoggedModel.class)
                                .build();
                    }

                    if (!lID.isEmpty()){
                        Query query2 = db.collection("Foods")
                                .whereIn("ID", lID);
                        options2 = new FirestorePagingOptions.Builder<FoodsLoggedModel>()
                                .setLifecycleOwner(DietPlansActivity.this)
                                .setQuery(query2, config,FoodsLoggedModel.class)
                                .build();
                    }

                    if (!dID.isEmpty()){
                        Query query3 = db.collection("Foods")
                                .whereIn("ID", dID);
                        options3 = new FirestorePagingOptions.Builder<FoodsLoggedModel>()
                                .setLifecycleOwner(DietPlansActivity.this)
                                .setQuery(query3, config,FoodsLoggedModel.class)
                                .build();
                    }

                    if (!sID.isEmpty()){
                        Query query4 = db.collection("Foods")
                                .whereIn("ID", sID);
                        options4 = new FirestorePagingOptions.Builder<FoodsLoggedModel>()
                                .setLifecycleOwner(DietPlansActivity.this)
                                .setQuery(query4, config,FoodsLoggedModel.class)
                                .build();
                    }


                    double btotal = 0;
                    double ltotal = 0;
                    double dtotal = 0;
                    double stotal = 0;

                    ArrayList<String> bkcalList = new ArrayList<String>();
                    for (int i = 0; i<bSize;i++){
                        String[] temp = breakfast.get(i).split("HOWMUCH");
                        bID.add(temp[0]);
                        bkcalList.add(temp[1]);
                        temp = temp[1].split("TS");
                        btotal = btotal + Double.parseDouble(temp[0]);
                    }

                    ArrayList<String> lkcalList = new ArrayList<String>();
                    for (int i = 0; i<lSize;i++){
                        String[] temp = lunch.get(i).split("HOWMUCH");
                        lID.add(temp[0]);
                        lkcalList.add(temp[1]);
                        temp = temp[1].split("TS");
                        ltotal = ltotal + Double.parseDouble(temp[0]);
                    }

                    ArrayList<String> dkcalList = new ArrayList<String>();
                    for (int i = 0; i<dSize;i++){
                        String[] temp = dinner.get(i).split("HOWMUCH");
                        dID.add(temp[0]);
                        dkcalList.add(temp[1]);
                        temp = temp[1].split("TS");
                        dtotal = dtotal + Double.parseDouble(temp[0]);
                    }

                    ArrayList<String> skcalList = new ArrayList<String>();
                    for (int i = 0; i<sSize;i++){
                        String[] temp = snack.get(i).split("HOWMUCH");
                        sID.add(temp[0]);
                        skcalList.add(temp[1]);
                        temp = temp[1].split("TS");
                        stotal = stotal + Double.parseDouble(temp[0]);
                    }
                    if (!bID.isEmpty()){
                        adapter1 = new FirestoreAdapterLogged(options1, DietPlansActivity.this, bkcalList, bnames);
                        breakfastFoods.setLayoutManager(new LinearLayoutManager(DietPlansActivity.this));
                        breakfastFoods.setAdapter(adapter1);
                        breakfastkcal.setText(String.valueOf((int)Math.round(btotal)));
                        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                            @Override
                            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                                return false;
                            }

                            @Override
                            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                                adapter1.deleteItemBreakfast(viewHolder.getAdapterPosition());
                                Handler handler = new Handler();
                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        change = new Intent(DietPlansActivity.this, DietPlansActivity.class);
                                        startActivity(change);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    }
                                };
                                handler.postDelayed(r, 100);
                            }
                        }).attachToRecyclerView(breakfastFoods);
                    }



                    if (!lID.isEmpty()){
                        adapter2 = new FirestoreAdapterLogged(options2, DietPlansActivity.this, lkcalList, lnames);
                        lunchFoods.setLayoutManager(new LinearLayoutManager(DietPlansActivity.this));
                        lunchFoods.setAdapter(adapter2);
                        lunchkcal.setText(String.valueOf((int)Math.round(ltotal)));
                    }

                    if (!dID.isEmpty()){
                        adapter3 = new FirestoreAdapterLogged(options3, DietPlansActivity.this, dkcalList, dnames);
                        dinnerFoods.setLayoutManager(new LinearLayoutManager(DietPlansActivity.this));
                        dinnerFoods.setAdapter(adapter3);
                        dinnerkcal.setText(String.valueOf((int)Math.round(dtotal)));
                    }

                    if (!sID.isEmpty()){
                        adapter4 = new FirestoreAdapterLogged(options4, DietPlansActivity.this, skcalList, snames);
                        snackFoods.setLayoutManager(new LinearLayoutManager(DietPlansActivity.this));
                        snackFoods.setAdapter(adapter4);
                        snackkcal.setText(String.valueOf((int)Math.round(stotal)));
                    }


                    int alltotal = (int)Math.round(btotal + ltotal + dtotal + stotal);
                    int g0 = (foodLogged.get("targetkcal") == null) ? 0 : Integer.parseInt(foodLogged.get("targetkcal").toString());
                    int goal0 = g0;
                    food.setText(String.valueOf(alltotal));
                    goal.setText(String.valueOf(goal0));
                    if (alltotal>goal0){
                        remaining.setText(String.valueOf(alltotal-goal0));
                        remainingcol.setTextColor(Color.parseColor("#E10606"));
                        remainingcol.setText("Exceeding");
                    }
                    else {
                        remaining.setText(String.valueOf(goal0-alltotal));
                        remainingcol.setTextColor(Color.parseColor("#06E106"));
                    }
                }

            }
        });













        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:
                        change = new Intent(DietPlansActivity.this, AccountActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.exercises:
                        change = new Intent(DietPlansActivity.this, ExercisesActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.workouts:
                        change = new Intent(DietPlansActivity.this, WorkoutsActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.foods:
                        change = new Intent(DietPlansActivity.this, NutritionActivity.class);
                        change.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(change);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.diets:
                        change = new Intent(DietPlansActivity.this, DietPlansActivity.class);
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
        adapter1.stopListening();
        adapter2.stopListening();
        adapter3.stopListening();
        adapter4.stopListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter1.startListening();
        adapter2.startListening();
        adapter3.startListening();
        adapter4.startListening();
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        Log.d("ITEM_CLICK", "Clicked: " + position + "ID is: " + snapshot.getId());
        Intent change = new Intent(DietPlansActivity.this, FoodProfile.class);
        change.putExtra("foodcategory","nothing");
        change.putExtra("id",snapshot.getId());
        startActivity(change);
    }
}