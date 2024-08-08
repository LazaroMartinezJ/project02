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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        Button createAccountButton = findViewById(R.id.create_account_button);
        Button adminButton = findViewById(R.id.admin_button);

       db = AppDataBase.getDatabase(getApplicationContext());

        loginButton.setOnClickListener(v -> login());
        createAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    CreateAccountActivity.class);
            startActivity(intent);
        });
        adminButton.setOnClickListener(v -> adminLogin());
    }


    private void login(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        new Thread(()-> {
                User user = db.userDao().getUser(username,password);
                runOnUiThread(() -> {
                    if (user != null) {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("USER_ID", user.id);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        }).start();
    }

    private void adminLogin(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        new Thread(() -> {
            Admin admin = db.adminDao().getAdmin(username, password);
            runOnUiThread(() -> {
                if(admin != null){
                    Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(MainActivity.this, "Invalid admin username or password", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

}