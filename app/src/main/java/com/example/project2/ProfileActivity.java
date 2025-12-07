package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

        // gets user Id
        int userId = getIntent().getIntExtra(LoginActivity.EXTRA_USER_ID, -1);
        if (userId == -1) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // retrieves user by id and displays their username and role
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

        // When 'Back' is clicked, take user back to LandingPageActivity
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });

        // When 'Change Password' is clicked, take user to PasswordActivity
        binding.changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });

        // logout
        binding.logoutButton.setOnClickListener(v -> {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });


    }

    static Intent profileIntentFactory(Context context) {
        return new Intent(context,ProfileActivity.class);
    }
}