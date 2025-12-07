package com.example.project2.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "challengeTable")
public class Challenge {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String question;

    private double correctAnswer;

    // when the challenge was created/sent (System.currentTimeMillis())
    private long createdAtMillis;

    public Challenge(@NonNull String question, double correctAnswer, long createdAtMillis) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.createdAtMillis = createdAtMillis;
    }

    // --- getters & setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getQuestion() {
        return question;
    }

    public void setQuestion(@NonNull String question) {
        this.question = question;
    }

    public double getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(double correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public long getCreatedAtMillis() {
        return createdAtMillis;
    }

    public void setCreatedAtMillis(long createdAtMillis) {
        this.createdAtMillis = createdAtMillis;
    }
}
