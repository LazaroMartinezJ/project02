package com.example.project2;
//@authorLAzaro MArtinez
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.project2.db.AppDataBase;

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
            User user = db.userDao().login(username,password);
            runOnUiThread(()->{
                if(user!=null){
                    Intent intent = new Intent(MainActivity.this,
                            HomeActivity.class);
                    intent.putExtra("USER_ID", user.id);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Invalid",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

}