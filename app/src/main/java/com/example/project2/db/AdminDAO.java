package com.example.project2.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project2.Admin;

@Dao
public interface AdminDAO {
    @Insert
    void insert(Admin admin);

    @Query("SELECT * FROM admin WHERE username = :username AND password = :password LIMIT 1")
    Admin getAdmin(String username, String password);
}
