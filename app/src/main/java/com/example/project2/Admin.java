package com.example.project2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "admin")
public class Admin {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String username;
    public String password;
}
