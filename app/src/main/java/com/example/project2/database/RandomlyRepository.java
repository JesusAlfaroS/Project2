package com.example.project2.database;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.project2.database.entities.Randomly;
import com.example.project2.MainActivity;
import com.example.project2.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class RandomlyRepository {
    private final RandomlyDAO randomlyDAO;
    private final UserDAO userDAO;
    private ArrayList<Randomly> allLogs;

    private static RandomlyRepository repository;

    private RandomlyRepository(Application application) {
        RandomlyDatabase db = RandomlyDatabase.getDatabase(application);
        this.randomlyDAO = db.randomlyDAO();
        this.userDAO = db.userDAO();
        this.allLogs = (ArrayList<Randomly>) this.randomlyDAO.getAllRecords();
    }

    public static RandomlyRepository getRepository(Application application){
        if(repository != null) {
            return repository;
        }
        Future<RandomlyRepository> future = RandomlyDatabase.databaseWriteExecutor.submit(
                new Callable<RandomlyRepository>() {
                    @Override
                    public RandomlyRepository call() throws Exception {
                        return new RandomlyRepository(application);
                    }
                }
        );
        try{
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(MainActivity.TAG,"Problem getting GymLogRepository, thread error.");
        }
        return null;
    }

    public ArrayList<Randomly> getAllLogs() {
        Future<ArrayList<Randomly>> future = RandomlyDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<Randomly>>() {
                    @Override
                    public ArrayList<Randomly> call() throws Exception {
                        return (ArrayList<Randomly>) randomlyDAO.getAllRecords();
                    }
                });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting all GymLogs in repository");
        }
        return null;
    }

    public void insertGymLog(Randomly randomly) {
        RandomlyDatabase.databaseWriteExecutor.execute(() ->
        {
            randomlyDAO.insert(randomly);
        });
    }

    public void insertUser(User... user) {
        RandomlyDatabase.databaseWriteExecutor.execute(() ->
        {
            userDAO.insert(user);
        });
    }

    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }

    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }

    public LiveData<List<Randomly>> getAllLogsByUserIdLiveData(int loggedInUserId) {
        return randomlyDAO.getRecordsByUserIdLiveData(loggedInUserId);
    }

    @Deprecated
    public ArrayList<Randomly> getAllLogsByUserId(int loggedInUserId) {
        Future<ArrayList<Randomly>> future = RandomlyDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<Randomly>>() {
                    @Override
                    public ArrayList<Randomly> call() throws Exception {
                        return (ArrayList<Randomly>) randomlyDAO.getRecordsByUserId(loggedInUserId);
                    }
                });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting all GymLogs in repository");
        }
        return null;
    }
}
