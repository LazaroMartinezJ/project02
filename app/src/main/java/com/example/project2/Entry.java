package com.example.project2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "entry")
public class Entry {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "content")
    public String content;

}
