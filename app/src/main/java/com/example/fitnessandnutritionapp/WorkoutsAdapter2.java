package com.example.fitnessandnutritionapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class WorkoutsAdapter2 extends FirestorePagingAdapter<WorkoutsModel, WorkoutsAdapter2.WorkoutsViewHolder> {

    private OnListItemClick onListItemClick;

    public WorkoutsAdapter2(@NonNull FirestorePagingOptions<WorkoutsModel> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkoutsViewHolder holder, int position, @NonNull WorkoutsModel model) {
        holder.name.setText(model.getName());
        Picasso.get().load(model.getPhoto()).into(holder.image);
        holder.image.setAlpha(0.85f);
        holder.category.setText(model.getCategory());
        holder.duration.setText(model.getDuration() + " min.");
    }

    @NonNull
    @Override
    public WorkoutsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_workout, parent, false);
        return new WorkoutsViewHolder(view);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
        switch (state){
            case LOADING_INITIAL:
                Log.d("PAGING_LOG", "Loading Initial Data");
                break;
            case LOADING_MORE:
                Log.d("PAGING_LOG", "Loading Next Page");
                break;
            case FINISHED:
                Log.d("PAGING_LOG", "All Data Loaded");
                break;
            case ERROR:
                Log.d("PAGING_LOG", "Error Loading Data");
                break;
            case LOADED:
                Log.d("PAGING_LOG", "Items Loaded: " + getItemCount());
                break;
        }
    }

    public class WorkoutsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView name;
        private ImageView image;
        private TextView category;
        private TextView duration;

        public WorkoutsViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.photo);
            category = itemView.findViewById(R.id.category);
            duration = itemView.findViewById(R.id.duration);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onListItemClick.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick{
        void onItemClick(DocumentSnapshot snapshot, int position);
    }
}
