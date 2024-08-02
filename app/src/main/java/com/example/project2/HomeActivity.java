package com.example.project2;


import android.content.Intent;
import android.os.Bundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        entryContentEditText = findViewById(R.id.entry_content);
        entryListTextView = findViewById(R.id.entry_list);
        Button saveEntryButton= findViewById(R.id.save_entry_button);
        Button entriesButton = findViewById(R.id.entries_button);
        Button singOutButton = findViewById(R.id.sign_out_button);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class, "User-database").build();
        userId = getIntent().getIntExtra("USER_ID", -1);

        saveEntryButton.setOnClickListener(v->saveEntry());
        entriesButton.setOnClickListener(v -> loadEntries());
        singOutButton.setOnClickListener(v -> signOut());

    }

    private void signOut() {
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
                loadEntries();
                entryContentEditText.setText("");
            });
        }).start();
    }

    private void loadEntries(){
        new Thread(()->{
            List<Entry> entries = db.entryDao().getEntriesForUser(userId);
            runOnUiThread(()->{
                StringBuilder entryText = new StringBuilder();
                for(Entry entry : entries){
                    entryText.append(entry.content).append("\n");
                }
                entryListTextView.setText(entryText.toString());
            });
        }).start();
    }


}
