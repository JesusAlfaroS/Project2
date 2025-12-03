package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.project2.database.RandomlyRepository;
import com.example.project2.database.entities.User;
import com.example.project2.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    // single key to pass userId to next screens
    public static final String EXTRA_USER_ID = "com.example.project2.EXTRA_USER_ID";

    private ActivityLoginBinding binding;
    private RandomlyRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = RandomlyRepository.getRepository(getApplication());

        // 🔐 One-time safety net to ensure default users exist
        ensureDefaultUsersSeeded();

        // login button
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { verifyUser(); }
        });

        // sign up (placeholder)
        binding.newUserSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { toastMaker("Sign-up not implemented yet"); }
        });
    }

    /**
     * Ensures 'admin2/admin2' (admin) and 'testuser1/testuser1' (non-admin) exist.
     * Runs once; if 'admin2' already exists, it does nothing.
     */
    private void ensureDefaultUsersSeeded() {
        repository.getUserByUserName("admin2").observe(this, existing -> {
            if (existing == null) {
                // Insert admin2 (admin)
                User admin = new User("admin2", "admin2");
                admin.setAdmin(true);
                repository.insertUser(admin);

                // Insert testuser1 (non-admin)
                User test = new User("testuser1", "testuser1");
                test.setAdmin(false);
                repository.insertUser(test);

                Toast.makeText(this, "Default users added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyUser() {
        String username = binding.userNameEditText.getText().toString().trim();   // trim
        if (username.isEmpty()) {
            toastMaker("Username should not be blank!");
            binding.userNameEditText.requestFocus();
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if (user != null) {
                String password = binding.passwordEditText.getText().toString().trim();  // trim
                if (password.equals(user.getPassword())) {
                    // success → go to Landing and pass userId
                    Intent i = new Intent(this, LandingPageActivity.class);
                    i.putExtra(EXTRA_USER_ID, user.getId());
                    startActivity(i);
                    finish();
                } else {
                    toastMaker("Invalid password");
                    binding.passwordEditText.setSelection(0);
                    binding.passwordEditText.requestFocus();
                }
            } else {
                toastMaker(String.format("%s is not a valid username.", username));
                binding.userNameEditText.setSelection(0);
                binding.userNameEditText.requestFocus();
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent loginIntentFactory(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
