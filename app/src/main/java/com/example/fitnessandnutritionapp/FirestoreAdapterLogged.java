package com.example.fitnessandnutritionapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class FirestoreAdapterLogged extends FirestorePagingAdapter<FoodsLoggedModel, FirestoreAdapterLogged.FoodsViewHolder> {

    private OnListItemClick onListItemClick;
    private ArrayList<String> arrayList;
    private ArrayList<String> arrayListNames;
    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
    private FirebaseAuth Auth= FirebaseAuth.getInstance();
    String UID = Auth.getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirestoreAdapterLogged(@NonNull FirestorePagingOptions<FoodsLoggedModel> options, OnListItemClick onListItemClick, ArrayList<String> arrayList, ArrayList<String> arrayListNames) {
        super(options);
        this.onListItemClick = onListItemClick;
        this.arrayList = arrayList;
        this.arrayListNames = arrayListNames;
    }

    @Override
    protected void onBindViewHolder(@NonNull FoodsViewHolder holder, int position, @NonNull FoodsLoggedModel model) {

        String[] timekcal = arrayList.get(position).split("TS");
        holder.list_name.setText(arrayListNames.get(position));
        holder.list_calories.setText(timekcal[0]);
        holder.list_time.setText(sdf1.format(new Date (Long.parseLong((timekcal[1])))));
    }

    @NonNull
    @Override
    public FoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_foods_single,parent,false);
        return new FoodsViewHolder(view);
    }

    public void deleteItemBreakfast(final int position){
        final DocumentReference documentReference = db.collection("Users").document(UID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snap = task.getResult();
                ArrayList<String> delete = (ArrayList<String>) snap.get("breakfast");
                documentReference.update("breakfast", FieldValue.arrayRemove((String) delete.get(position)));
            }
        });

    }

    public class FoodsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView list_name;
        private TextView list_calories;
        private TextView list_time;

        public FoodsViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.list_name);
            list_calories = itemView.findViewById(R.id.list_calories);
            list_time = itemView.findViewById(R.id.list_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onListItemClick.onItemClick(getItem(getAdapterPosition()),getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(DocumentSnapshot snapshot,int position);
    }

}
