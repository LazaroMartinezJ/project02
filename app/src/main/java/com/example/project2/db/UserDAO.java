package com.example.project2.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project2.User;

@Dao
public interface UserDAO {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM user WHERE username = :username AND password = :password" )
    User login(String username, String password);
}
