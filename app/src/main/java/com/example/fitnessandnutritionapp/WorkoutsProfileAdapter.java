package com.example.fitnessandnutritionapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WorkoutsProfileAdapter extends RecyclerView.Adapter<WorkoutsProfileAdapter.MyViewHolder> {

    String exnames[], sr[], r[];
    Context context;
    private FirebaseFirestore db;
    String [] musclegroups = new String [] {"Biceps","Calves", "Forearms",
        "FrontDeltoids","Glutes","Hamstrings","InnerBack","LowerAbs","LowerBack",
        "LowerChest","MiddleAbs","MiddleChest","Obliques","Quadriceps","RearDeltoids",
        "SideDeltoids","Traps","Triceps","UpperAbs","UpperChest","WiderBack"};



    public WorkoutsProfileAdapter(Context ct, ArrayList<String> exercisesnames, ArrayList<String> setsreps, ArrayList<String> rest){
        context = ct;
        exnames = exercisesnames.toArray(new String[0]);
        sr = setsreps.toArray(new String[0]);
        r = rest.toArray(new String[0]);
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workout_profile_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.name.setText(exnames[position]);
        holder.setsreps.setText(sr[position]);
        holder.rest.setText(r[position]);
        int j = 0;
        final boolean[] b = {false};
        while ((b[0] ==false) && (j<21)){
            db.collection(musclegroups[j])
                    .whereEqualTo("name",exnames[position])
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    if (document.exists()){
                                        //EXISTS
                                        String photourl = (String) document.get("URL");
                                        Picasso.get().load(photourl).into(holder.image);
                                        b[0] = true;
                                    }
                                }
                            }
                            else{
                                //NOT SUCCESSFULL
                            }
                        }
                    });
            j = j + 1;
        }



    }

    @Override
    public int getItemCount() {
        Log.d("SIZEEEE----   ","is     " + exnames.length);
        return exnames.length;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, setsreps, rest;
        ImageView image;
        RelativeLayout relativeLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            setsreps = itemView.findViewById(R.id.setsreps);
            rest = itemView.findViewById(R.id.rest);
            image = itemView.findViewById(R.id.image);
            relativeLayout = itemView.findViewById(R.id.relativeLayout2);
        }
    }
}
