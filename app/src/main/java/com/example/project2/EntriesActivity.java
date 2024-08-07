package com.example.project2;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.project2.db.AppDataBase;

import java.util.ArrayList;
import java.util.List;

public class EntriesActivity extends AppCompatActivity {
    private AppDataBase db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);

        db = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "user-database").build();
        userId = getIntent().getIntExtra("USER_ID", -1);

        ListView entriesListView = findViewById(R.id.entries_list_view);

        new Thread(() -> {
            List<Entry> entries = db.entryDao().getEntriesForUser(userId);
            List<String> entryContents = new ArrayList<>();
            for(Entry entry : entries){
                entryContents.add(entry.content);
            }
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, entryContents);
                entriesListView.setAdapter(adapter);
            });
        }).start();
    }
}
