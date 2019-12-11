package com.hanyi.mapsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        final Button signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataProvider.getInstance(getApplicationContext()).signIn(
                    username.getText().toString(),
                    password.getText().toString(),
                    new IDataCallback<User>(){
                        @Override
                        public void onComplete(User user) {
                            User.setLoggedInUser(user);
                            Intent intent = new Intent(LoginActivity.this, MapViewActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailed(Exception exception) {
                            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG);
                        }
                    }
                );
            }
        });

        final Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });
    }
}
