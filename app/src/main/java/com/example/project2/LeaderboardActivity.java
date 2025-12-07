package com.example.project2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project2.database.RandomlyRepository;
import com.example.project2.databinding.ActivityLeaderboardBinding;

public class LeaderboardActivity extends AppCompatActivity {

    private ActivityLeaderboardBinding binding;
    private RandomlyRepository repository;
    private UserLeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = RandomlyRepository.getRepository(getApplication());

        adapter = new UserLeaderboardAdapter();
        binding.leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.leaderboardRecyclerView.setAdapter(adapter);

        // Observe all users sorted by points (LiveData)
        repository.getAllUsersByPoints().observe(this, users -> {
            adapter.setUsers(users);
        });
    }
}
