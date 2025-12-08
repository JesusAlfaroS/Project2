package com.example.project2.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project2.database.entities.Randomly;
import com.example.project2.database.entities.User;

import java.util.List;

@Dao
public interface RandomlyDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Randomly randomly);

    // use literal table name to avoid annotation-processor complaints
    @Query("SELECT * FROM Randomly ORDER BY date DESC")
    List<Randomly> getAllRecords();

    @Query("SELECT * FROM Randomly WHERE userId = :loggedInUserId ORDER BY date DESC")
    List<Randomly> getRecordsByUserId(int loggedInUserId);

    @Query("SELECT * FROM Randomly WHERE userId = :loggedInUserId ORDER BY date DESC")
    LiveData<List<Randomly>> getRecordsByUserIdLiveData(int loggedInUserId);
}
