package com.example.project2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.project2.db.AppDataBase;

public class AdminHomeActivity extends AppCompatActivity {
    private EditText newAdminUsernameEditText, newAdminPasswordEditText;
    private Button createAdminButton;
    private AppDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        newAdminUsernameEditText = findViewById(R.id.new_admin_username);
        newAdminPasswordEditText = findViewById(R.id.new_admin_password);
        createAdminButton = findViewById(R.id.create_admin_button);

        db = AppDataBase.getDatabase(getApplicationContext());
        createAdminButton.setOnClickListener(v -> createAdmin());
    }

    private void createAdmin(){
        String username = newAdminUsernameEditText.getText().toString();
        String password = newAdminPasswordEditText.getText().toString();

        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Admin admin = new Admin();
            admin.username = username;
            admin.password = password;
            db.adminDao().insert(admin);
            runOnUiThread(() -> Toast.makeText(AdminHomeActivity.this,
                    "New admin created", Toast.LENGTH_SHORT).show());
        }).start();
    }
}
