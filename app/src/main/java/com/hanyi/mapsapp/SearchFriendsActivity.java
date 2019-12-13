package com.hanyi.mapsapp;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchFriendsActivity extends AppCompatActivity {
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        final ListView listView = findViewById(R.id.findFriendsResult);
        final ImageButton searchFriendsButton = findViewById(R.id.searchFriendsButton);
        final EditText searchFriendsEditText = findViewById(R.id.searchFriendsEditText);

        //String[] str = {"新北市","台北市","台中市","台南市","高雄市"};


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
                            friendNames.add(friend.firstName + " " + friend.lastName);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(SearchFriendsActivity.this, android.R.layout.simple_list_item_1, friendNames);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        //
                    }
                });
            }
        });
    }
}
