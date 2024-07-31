package com.example.project2;


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

        db = Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class, "User-database").build();
        userId = getIntent().getIntExtra("USER_ID", -1);

        saveEntryButton.setOnClickListener(v->saveEntry());
        loadEntries();

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
