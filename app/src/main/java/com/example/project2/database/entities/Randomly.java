package com.example.project2.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.project2.database.typeConverters.LocalDateTypeConverter;

import java.time.LocalDateTime;

@Entity(tableName = "gymLogTable")
@TypeConverters(LocalDateTypeConverter.class)
public class Randomly {

    @PrimaryKey(autoGenerate = true)
    private int id;

    // Owner / creator of the log
    private int userId;

    // Stored via LocalDateTypeConverter (LocalDateTime <-> long epoch millis)
    private LocalDateTime date;

    // Optional note/description
    private String note;

    public Randomly(int userId, LocalDateTime date, String note) {
        this.userId = userId;
        this.date = date;
        this.note = note;
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
