package com.hanyi.mapsapp;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchFriendsActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private Map<String, Integer> nameToId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        nameToId = new HashMap<>();
        final ListView listView = findViewById(R.id.findFriendsResult);
        final ImageButton searchFriendsButton = findViewById(R.id.searchFriendsButton);
        final EditText searchFriendsEditText = findViewById(R.id.searchFriendsEditText);

        searchFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataProvider dataProvider = DataProvider.getInstance(getApplicationContext());
                String text = searchFriendsEditText.getText().toString();
                dataProvider.searchFriends(text, new IDataCallback<ArrayList<User>>() {
                    @Override
                    public void onComplete(ArrayList<User> friends) {
                        ArrayList<String> friendNames = new ArrayList<>();
                        for (User friend: friends) {
                            String name = friend.firstName + " " + friend.lastName;
                            nameToId.put(name, friend.id);
                            friendNames.add(name);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(
                            SearchFriendsActivity.this,
                            android.R.layout.simple_list_item_1,
                            friendNames
                        );
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        //
                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                final String name = (String) listView.getItemAtPosition(position);
                int friendId = nameToId.get(name);
                DataProvider.getInstance(getApplicationContext()).addFriend(friendId, new IDataCallback<Boolean>() {
                    @Override
                    public void onComplete(Boolean result) {
                        Toast.makeText(getApplicationContext(), "Added: " + name, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        Toast.makeText(getApplicationContext(), "Failed to add: " + name, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
