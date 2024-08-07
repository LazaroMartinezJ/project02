package com.example.project2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.project2.db.AppDataBase;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private EditText entryContentEditText;
    private TextView entryListTextView;
    private AppDataBase db;
    private int userId;
    private static final String PREFERENCES_FILE = "com.example.yourapp.preferences";
    private static final String KEY_USER_ID = "USER_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       entryContentEditText = findViewById(R.id.entry_content);
    //   entryListTextView = findViewById(R.id.entry_list);
        Button saveEntryButton= findViewById(R.id.save_entry_button);
        Button entriesButton = findViewById(R.id.entries_button);
        Button singOutButton = findViewById(R.id.sign_out_button);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class, "user-database").build();
        userId = getIntent().getIntExtra("USER_ID", -1);

        saveEntryButton.setOnClickListener(v->saveEntry());
        entriesButton.setOnClickListener(v -> openEntriesActivity());
        singOutButton.setOnClickListener(v -> signOut());

    }

    private void signOut() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("USER_ID");
        editor.apply();

        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void saveEntry(){
        String content = entryContentEditText.getText().toString();

        Entry entry = new Entry();
        entry.userId = userId;
        entry.content = content;

        new Thread(()-> {
            db.entryDao().insert(entry);
            runOnUiThread(()->{
                Toast.makeText(HomeActivity.this, "Entry saved",
                Toast.LENGTH_SHORT).show();
                entryContentEditText.setText("");
            });
        }).start();
    }

    private void openEntriesActivity(){
      Intent intent = new Intent(HomeActivity.this, EntriesActivity.class);
      intent.putExtra("USER_ID", userId);
      startActivity(intent);
    }


}
