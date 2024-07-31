package com.example.project2.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.project2.Entry;
import com.example.project2.User;

@Database(entities = {User.class, Entry.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract UserDAO userDao();

    public abstract EntryDAO entryDao();
}
