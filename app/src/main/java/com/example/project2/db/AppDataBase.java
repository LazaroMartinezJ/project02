package com.example.project2.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.project2.Admin;
import com.example.project2.Entry;
import com.example.project2.User;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Entry.class, Admin.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract UserDAO userDao();
    public abstract EntryDAO entryDao();
    public abstract AdminDAO adminDao();
    private static volatile AppDataBase INSTANCE;

    public static AppDataBase getDatabase(final Context context) {
        if(INSTANCE == null){
            synchronized (AppDataBase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, "user-database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                AdminDAO adminDao = INSTANCE.adminDao();
                Admin defaultAdmin = new Admin();
                defaultAdmin.username = "admin";
                defaultAdmin.password = "admin";
                adminDao.insert(defaultAdmin);
            });
        }
    };
}
