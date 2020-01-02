package com.hanyi.mapsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExploreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);

        DataProvider.getInstance(getApplicationContext()).getFriendPost(
                new IDataCallback<ArrayList<Post>>() {
                    @Override
                    public void onComplete(ArrayList<Post> postList) {
                        recyclerViewAdapter.setAllPost(postList);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        Button buttonPost;
        buttonPost = findViewById(R.id.buttonPost);
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExploreActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });
    }
}
