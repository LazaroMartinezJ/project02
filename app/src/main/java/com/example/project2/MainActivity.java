package com.example.project2;
//@authorLAzaro MArtinez
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.project2.db.AppDataBase;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private AppDataBase db;
    private static final String TAG = "MainActivity";
    private static final String PREFERENCE_FILE = "com.example.yourapp.preferences";
    private static final String KEY_USER_ID = "USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        int savedUserId = preferences.getInt("USER_ID", -1);
        if(savedUserId != -1){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("USER_ID", savedUserId);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        Button createAccountButton = findViewById(R.id.create_account_button);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class, "user-database").build();

        loginButton.setOnClickListener(v -> login());
        createAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    CreateAccountActivity.class);
            startActivity(intent);
        });
    }

    private void login(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        new Thread(()-> {
            try {
                User user = db.userDao().login(username, password);
                runOnUiThread(() -> {
                    if (user != null) {
                        SharedPreferences preferences = getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("USER_ID", user.id);
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("USER_ID", user.id);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e) {
                Log.e(TAG, "Error loggin in", e);
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

}