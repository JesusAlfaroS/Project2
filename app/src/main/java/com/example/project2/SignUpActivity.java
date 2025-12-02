package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.database.RandomlyRepository;
import com.example.project2.database.entities.User;
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

                createAccount(username, password);

            }
        });
    }

    /*
    * Checks if username already exits, if it does not then create user with
    * username and password passed through
    * If the username already exits displays a very generous text :>
     */
    private void createAccount(String username, String password) {
        repository.getUserByUserName(username).observe(this, existing -> {
            if (existing == null) {
                User admin = new User(username,password);
                admin.setAdmin(true);
                repository.insertUser(admin);

                Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
                startActivity(intent);

                Toast.makeText(this, "Account Successfully Created!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "THIS ACCOUNT ALREADY EXITS LOSER", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static Intent signUpIntentFactory(Context context) {
        return new Intent(context, SignUpActivity.class);
    }
}

