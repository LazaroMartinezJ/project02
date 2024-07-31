package com.example.project2.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project2.Entry;

import java.util.List;

@Dao
public interface EntryDAO {
    @Insert
    void insert(Entry entry);

    @Query("SELECT * FROM entry WHERE user_id = :userId")
    List<Entry> getEntriesForUser(int userId);
}
