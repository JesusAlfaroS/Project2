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
import com.example.project2.database.entities.User;
import com.example.project2.databinding.ActivityLoginBinding;
import com.example.project2.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private RandomlyRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = RandomlyRepository.getRepository(getApplication());

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gets username and password
                String username = binding.userNameSignUpEditText.getText().toString().trim();   // trim
                String password = binding.passwordSignUpEditText.getText().toString().trim();   // trim

                // creates user with created username and password
                User admin = new User(username,password);
                admin.setAdmin(true);
                repository.insertUser(admin);

                // Launches login activity
                Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "login clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static Intent signUpIntentFactory(Context context) {
        return new Intent(context, SignUpActivity.class);
    }
}

