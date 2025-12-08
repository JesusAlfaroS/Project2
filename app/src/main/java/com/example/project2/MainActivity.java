package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.database.RandomlyRepository;
import com.example.project2.database.entities.User;

public class MainActivity extends AppCompatActivity {

    private RandomlyRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repository = RandomlyRepository.getRepository(getApplication());

        // 1) Check if someone is already logged in
        int savedUserId = Prefs.getLoggedInUserId(this);
        if (savedUserId != -1) {
            // Look up the user in the DBase
            repository.getUserByUserId(savedUserId).observe(this, user -> {
                if (user == null) {
                    // bad id → clear and show normal welcome screen
                    Prefs.clearLoggedInUser(this);
                    showWelcomeScreen();
                } else {
                    // valid user → jump straight to correct page
                    goToHomeForUser(user);
                }
            });
        } else {
            // No saved session → normal welcome + buttons
            showWelcomeScreen();
        }
    }

    // Show the actual original welcome screen with LOG IN / SIGN UP buttons
    private void showWelcomeScreen() {
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        loginButton.setOnClickListener(v -> {
            Intent i = LoginActivity.loginIntentFactory(this);
            startActivity(i);
        });

        signUpButton.setOnClickListener(v -> {
            Intent i = new Intent(this, SignUpActivity.class);
            startActivity(i);
        });
    }

    // Send the user to Admin page or User page
    private void goToHomeForUser(User user) {
        Intent i;
        if (user.isAdmin()) {
            i = new Intent(this, LandingPageActivity.class);
        } else {
            i = new Intent(this, UserPageActivity.class);
        }
        i.putExtra(LoginActivity.EXTRA_USER_ID, user.getId());
        startActivity(i);
        finish();   // don't come back to MainActivity when pressing back
    }
}
