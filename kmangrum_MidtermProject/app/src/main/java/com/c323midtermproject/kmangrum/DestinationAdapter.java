package com.c323midtermproject.kmangrum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.myViewHolder> {
    private ArrayList<DestinationItem> mDestinationList;

    public static class myViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public TextView textViewVisit;
        public TextView textViewLocation;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
//            Get the different layout elements
            textViewName = itemView.findViewById(R.id.tv1);
            textViewVisit = itemView.findViewById(R.id.tvCardBestTimeToVisit);
            textViewLocation = itemView.findViewById(R.id.tv2);
        }
    }

    public DestinationAdapter(ArrayList<DestinationItem> destinationList) {
        mDestinationList = destinationList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_cardview_layout, parent, false);
        myViewHolder mvh = new myViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        DestinationItem currentItem = mDestinationList.get(position);

//        Display the different destination items information in the cardview's textviews
        holder.textViewName.setText("Name: " + currentItem.getName());
        holder.textViewVisit.setText("Best Time to Visit: " + currentItem.getTime());
        holder.textViewLocation.setText("Location: " + currentItem.getdLocation());

    }

    @Override
    public int getItemCount() {
        return mDestinationList.size();
    }
}
