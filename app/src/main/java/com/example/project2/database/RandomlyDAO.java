package com.example.project2.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project2.database.entities.Randomly;

import java.util.List;

@Dao
public interface RandomlyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Randomly randomly);

    @Query("Select * from " + RandomlyDatabase.randomlyTable + " ORDER BY date DESC")
    List<Randomly> getAllRecords();


    @Query("Select * from " + RandomlyDatabase.randomlyTable + " WHERE userId = :loggedInUserId ORDER BY date DESC")
    List<Randomly> getRecordsByUserId(int loggedInUserId);

    @Query("Select * from " + RandomlyDatabase.randomlyTable + " WHERE userId = :loggedInUserId ORDER BY date DESC")
    LiveData<List<Randomly>> getRecordsByUserIdLiveData(int loggedInUserId);
}
