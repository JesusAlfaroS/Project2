package com.example.project2.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project2.database.RandomlyDatabase;

import java.util.Objects;

@Entity(tableName = RandomlyDatabase.USER_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;
    private String password;
    private boolean isAdmin;

    // Total points the user has earned from challenges
    private int points;

    // The ID of the last challenge this user has already been scored for
    // -1 means "none yet"
    private int lastSolvedChallengeId;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isAdmin = false;
        this.points = 0;
        this.lastSolvedChallengeId = -1;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id
                && isAdmin == user.isAdmin
                && points == user.points
                && lastSolvedChallengeId == user.lastSolvedChallengeId
                && Objects.equals(username, user.username)
                && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, isAdmin, points, lastSolvedChallengeId);
    }

    // --- Getters & Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getLastSolvedChallengeId() {
        return lastSolvedChallengeId;
    }

    public void setLastSolvedChallengeId(int lastSolvedChallengeId) {
        this.lastSolvedChallengeId = lastSolvedChallengeId;
    }
}
