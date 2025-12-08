package com.example.project2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.database.RandomlyRepository;
import com.example.project2.database.entities.User;
import com.example.project2.databinding.ActivityLandingPageBinding;

public class LandingPageActivity extends AppCompatActivity {

    private ActivityLandingPageBinding binding;
    private RandomlyRepository repository;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = RandomlyRepository.getRepository(getApplication());

        // Get the logged in userId
        userId = getIntent().getIntExtra(LoginActivity.EXTRA_USER_ID, -1);
        if (userId == -1) {
            // invalid session → go to login
            Intent i = LoginActivity.loginIntentFactory(this);
            startActivity(i);
            finish();
            return;
        }

        // Load the admin user and display username + any role
        repository.getUserByUserId(userId).observe(this, user -> {
            if (user != null) {
                bindUser(user);
            }
        });

        // CREATE CHALLENGE button
        binding.createChallengeButton.setOnClickListener(v -> {
            Intent i = new Intent(this, CreateChallengeActivity.class);
            i.putExtra(LoginActivity.EXTRA_USER_ID, userId);
            startActivity(i);
        });

        // LOGOUT button
        binding.profile.setOnClickListener(v -> {
            Intent intent = ProfileActivity.profileIntentFactory(this,userId);
            startActivity(intent);
        });
    }

    private void bindUser(User user) {
        // These IDs MUST match your XML
        binding.landingUsernameTextView.setText(user.getUsername());
        binding.landingRoleTextView.setText(user.isAdmin() ? "Admin" : "User");
    }
}
