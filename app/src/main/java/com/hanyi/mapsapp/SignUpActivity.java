package com.hanyi.mapsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText username = findViewById(R.id.editTextUserName);
        final EditText password = findViewById(R.id.editTextPassword);
        final EditText reenterPassword = findViewById(R.id.editTextReenterPassword);
        final EditText residentId = findViewById(R.id.editTextResidentId);
        final EditText firstName = findViewById(R.id.editTextFirstName);
        final EditText lastName = findViewById(R.id.editTextLastName);
        final EditText phoneNumber = findViewById(R.id.editTextPhoneNumber);
        final Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty() &&
                        !reenterPassword.getText().toString().isEmpty() && !residentId.getText().toString().isEmpty() &&
                        !firstName.getText().toString().isEmpty() && !lastName.getText().toString().isEmpty() &&
                        !phoneNumber.getText().toString().isEmpty()) {
                    if (password.getText().toString().equals(reenterPassword.getText().toString())) {
                        DataProvider.getInstance(getApplicationContext()).signUp(
                                username.getText().toString(),
                                password.getText().toString(),
                                residentId.getText().toString(),
                                firstName.getText().toString(),
                                lastName.getText().toString(),
                                phoneNumber.getText().toString(),
                                new IDataCallback<Boolean>() {
                                    @Override
                                    public void onComplete(Boolean result) {
                                        if (result) {
                                            Toast.makeText(getApplicationContext(), "Success.", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Failed to create account.", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailed(Exception exception) {
                                        Toast.makeText(getApplicationContext(), "Wrong username or password.", Toast.LENGTH_LONG).show();
                                    }
                                }
                        );
                    } else {
                        Toast.makeText(getApplicationContext(), "Two password should be the same.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill out the form completely.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
