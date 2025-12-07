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
import com.example.project2.database.entities.User;
import com.example.project2.databinding.ActivityPasswordBinding;

public class PasswordActivity extends AppCompatActivity {

    private ActivityPasswordBinding binding;
    private RandomlyRepository repository;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = RandomlyRepository.getRepository(getApplication());

        // gets user Id
        int userId = getIntent().getIntExtra(LoginActivity.EXTRA_USER_ID, -1);
        if (userId == -1) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // retrieves user by id and assigns user to current user
        repository.getUserByUserId(userId).observe(this, user -> {
            if (user == null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            }
            boolean isAdmin = user.isAdmin();
            currentUser = user;
        });
    }

    static Intent passwordIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, PasswordActivity.class);
        intent.putExtra(LoginActivity.EXTRA_USER_ID,userId);
        return intent;
    }
}