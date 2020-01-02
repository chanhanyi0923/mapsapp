package com.hanyi.mapsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private List<Post> postList = new ArrayList<>();
    private Context context;

    RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    void setAllPost(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView;
        itemView = layoutInflater.inflate(R.layout.cell_post, parent, false);

        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        final Post post = postList.get(position);
        holder.textViewUserName.setText(post.getUsername());
        String location = "Location: " + post.getLatitude() + ", " + post.getLongitude();
        holder.textViewLocation.setText(location);
        String peopleNum = "People: " + post.getPeopleNum();
        holder.textViewPeopleNum.setText(peopleNum);
        String signalType = "Type: " + post.getSignalTypeName(context);
        holder.textViewSignalType.setText(signalType);
        String signalLevel = "Level: " + post.getSignalLevel();
        holder.textViewSignalLevel.setText(signalLevel);
        String message = "Message: \n" + post.getMessage();
        holder.textViewMessage.setText(message);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUserName, textViewLocation, textViewPeopleNum, textViewSignalType,
                textViewSignalLevel, textViewMessage;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewPeopleNum = itemView.findViewById(R.id.textViewPeopleNum);
            textViewSignalType = itemView.findViewById(R.id.textViewSignalType);
            textViewSignalLevel = itemView.findViewById(R.id.textViewSignalLevel);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
}
