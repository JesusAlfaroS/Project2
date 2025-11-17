package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.database.RandomlyRepository;
import com.example.project2.database.entities.User;
import com.example.project2.databinding.ActivityLandingPageBinding;

public class LandingPageActivity extends AppCompatActivity {

    private ActivityLandingPageBinding binding;
    private RandomlyRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = RandomlyRepository.getRepository(getApplication());

        // get the user id from LoginActivity
        int userId = getIntent().getIntExtra(LoginActivity.EXTRA_USER_ID, -1);
        if (userId == -1) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // load user from DB
        repository.getUserByUserId(userId).observe(this, user -> {
            if (user == null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            }
            bindUser(user);
        });

        // admin-only action (stub)
        binding.adminAreaButton.setOnClickListener(v ->
                Toast.makeText(this, "Admin: create a challenge!", Toast.LENGTH_SHORT).show()
        );

        // logout
        binding.logoutButton.setOnClickListener(v -> {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });
    }

    private void bindUser(User user) {
        binding.usernameText.setText(user.getUsername());
        boolean isAdmin = user.isAdmin();
        binding.roleText.setText(isAdmin ? "Admin" : "User");
        binding.adminAreaButton.setVisibility(isAdmin ? View.VISIBLE : View.INVISIBLE);
    }
}
