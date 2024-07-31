package com.example.project2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.project2.db.AppDataBase;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText newUsernameEditText;
    private EditText newPasswordEditText;
    private AppDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        newUsernameEditText = findViewById(R.id.new_username);
        newPasswordEditText = findViewById(R.id.new_password);
        Button saveAccountButton = findViewById(R.id.save_account_button);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class, "user-database").build();

        saveAccountButton.setOnClickListener(v -> createAccount());
    }

    private void createAccount(){
        String username = newUsernameEditText.getText().toString();
        String password = newPasswordEditText.getText().toString();

        User user = new User();
        user.username = username;
        user.password = password;

        new Thread(()-> {
            db.userDao().insert(user);
            runOnUiThread(()->{
                Toast.makeText(CreateAccountActivity.this, "Account Created",
                        Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
