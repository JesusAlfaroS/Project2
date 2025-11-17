package com.example.project2.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project2.database.RandomlyDatabase;

import java.time.LocalDateTime;

@Entity(tableName = RandomlyDatabase.randomlyTable)
public class Randomly {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private LocalDateTime date;
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
