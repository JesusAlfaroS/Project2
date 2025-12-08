package com.example.project2.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project2.database.entities.Challenge;

import java.util.List;

@Dao
public interface ChallengeDAO {

    @Insert
    void insert(Challenge challenge);

    // latest challenge (for "current problem" in UI)
    @Query("SELECT * FROM challengeTable ORDER BY createdAtMillis DESC LIMIT 1")
    LiveData<Challenge> getLatestChallenge();

    // all challenges (if you ever want history LATER)
    @Query("SELECT * FROM challengeTable ORDER BY createdAtMillis DESC")
    LiveData<List<Challenge>> getAllChallenges();

    // synchronous version for tests / internal use
    @Query("SELECT * FROM challengeTable ORDER BY createdAtMillis DESC LIMIT 1")
    Challenge getLatestChallengeSync();
}
