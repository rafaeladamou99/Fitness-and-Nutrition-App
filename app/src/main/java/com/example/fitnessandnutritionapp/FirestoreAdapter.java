package com.example.fitnessandnutritionapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class FirestoreAdapter extends FirestorePagingAdapter<musclegroupsmodel, FirestoreAdapter.MuscleGroupsViewHolder> {

    private OnListItemClick onListItemClick;

    public FirestoreAdapter(@NonNull FirestorePagingOptions<musclegroupsmodel> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull MuscleGroupsViewHolder holder, int position, @NonNull musclegroupsmodel model) {
        String text = model.getName();
        if (text.length()>30){
            text = text.substring(0,30);
        }
        holder.list_name.setText(text);
    }


    @NonNull
    @Override
    public MuscleGroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.musclegroupsitem,parent,false);
        return new MuscleGroupsViewHolder(view);
    }

    public class MuscleGroupsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView list_name;

        public MuscleGroupsViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.list_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onListItemClick.onItemClick(getItem(getAdapterPosition()) ,getAdapterPosition());
        }
    }

    public interface OnListItemClick{
        void onItemClick(DocumentSnapshot snapshot, int position);
    }
}
