package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project2.database.RandomlyRepository;
import com.example.project2.databinding.ActivityProfileBinding;
import com.example.project2.databinding.ActivitySignUpBinding;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private RandomlyRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = RandomlyRepository.getRepository(getApplication());

        int userId = getIntent().getIntExtra(LoginActivity.EXTRA_USER_ID, -1);
        if (userId == -1) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        repository.getUserByUserId(userId).observe(this, user -> {
            if (user == null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            }
            binding.userNameText.setText(user.getUsername());
            boolean isAdmin = user.isAdmin();
            binding.role.setText(isAdmin ? "Admin" : "User");
        });
    }

    static Intent profileIntentFactory(Context context) {
        return new Intent(context,ProfileActivity.class);
    }
}