package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.database.RandomlyRepository;
import com.example.project2.database.entities.Challenge;
import com.example.project2.database.entities.User;
import com.example.project2.databinding.ActivityUserPage2Binding;

public class UserPageActivity extends AppCompatActivity {

    private static final long CHALLENGE_WINDOW_MS = 30_000L; // 30 seconds

    private ActivityUserPage2Binding binding;
    private RandomlyRepository repository;

    private User currentUser;
    private Challenge currentChallenge;
    private CountDownTimer countDownTimer;
    private boolean hasAnsweredThisChallenge = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserPage2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = RandomlyRepository.getRepository(getApplication());

        // 1) Figure out which user is logged in
        int userId = getIntent().getIntExtra(LoginActivity.EXTRA_USER_ID, -1);
        if (userId == -1) {
            userId = Prefs.getLoggedInUserId(this);
        }
        if (userId == -1) {
            // Safety fallback: go back to login
            startActivity(LoginActivity.loginIntentFactory(this));
            finish();
            return;
        }

        // 2) Observe user from DB (for username + points)
        repository.getUserByUserId(userId).observe(this, user -> {
            if (user == null) {
                Toast.makeText(this, "User not found, please log in again.", Toast.LENGTH_SHORT).show();
                Prefs.clearLoggedInUser(this);
                startActivity(LoginActivity.loginIntentFactory(this));
                finish();
                return;
            }
            currentUser = user;
            binding.userPageUsernameTextView.setText(user.getUsername());
            binding.userPagePointsTextView.setText("Points: " + user.getPoints());
        });

        // 3) Observe latest challenge
        repository.getLatestChallenge().observe(this, challenge -> {
            currentChallenge = challenge;
            hasAnsweredThisChallenge = false; // reset for new challenge

            if (challenge == null) {
                binding.userPageChallengeTextView.setText("No challenge yet. Please wait for admin.");
                binding.userPageTimerTextView.setText("");
                binding.userPageSubmitButton.setEnabled(false);
                return;
            }

            binding.userPageChallengeTextView.setText(challenge.getQuestion());
            binding.userPageSubmitButton.setEnabled(true);

            // Setup timer based on challenge created time
            startOrUpdateTimer(challenge);
        });

        // 4) SUBMIT answer
        binding.userPageSubmitButton.setOnClickListener(v -> handleSubmit());

        // 5) LOG OUT
        binding.userPageLogoutButton.setOnClickListener(v -> {
            Prefs.clearLoggedInUser(this);
            Intent i = LoginActivity.loginIntentFactory(this);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });
    }

    // ---------- Timer logic ----------

    private void startOrUpdateTimer(Challenge challenge) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        long now = System.currentTimeMillis();
        long elapsed = now - challenge.getCreatedAtMillis();
        long remaining = CHALLENGE_WINDOW_MS - elapsed;

        if (remaining <= 0) {
            binding.userPageTimerTextView.setText("Time left: 0s (time's up)");
            binding.userPageSubmitButton.setEnabled(false);
            return;
        }

        countDownTimer = new CountDownTimer(remaining, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000L;
                binding.userPageTimerTextView.setText("Time left: " + seconds + "s");
            }

            @Override
            public void onFinish() {
                binding.userPageTimerTextView.setText("Time left: 0s (time's up)");
                binding.userPageSubmitButton.setEnabled(false);
            }
        };
        countDownTimer.start();
    }

    // ---------- Answer handling + scoring ----------

    private void handleSubmit() {
        if (currentUser == null || currentChallenge == null) {
            Toast.makeText(this, "No challenge to answer.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (hasAnsweredThisChallenge) {
            Toast.makeText(this, "You already answered this challenge.", Toast.LENGTH_SHORT).show();
            return;
        }

        String answerStr = binding.userPageAnswerEditText.getText().toString().trim();
        if (TextUtils.isEmpty(answerStr)) {
            Toast.makeText(this, "Please enter an answer.", Toast.LENGTH_SHORT).show();
            return;
        }

        double userAnswer;
        try {
            userAnswer = Double.parseDouble(answerStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Answer must be a number.", Toast.LENGTH_SHORT).show();
            return;
        }

        double correct = currentChallenge.getCorrectAnswer();
        boolean isCorrect = Math.abs(userAnswer - correct) < 1e-6;

        long now = System.currentTimeMillis();
        long elapsed = now - currentChallenge.getCreatedAtMillis();
        long remaining = CHALLENGE_WINDOW_MS - elapsed;
        if (remaining < 0) remaining = 0;

        int score = 0;
        if (isCorrect) {
            // simple decaying score: 1000 at t=0 → 10 at t>=30s
            score = (int) Math.max(10, 1000 - (elapsed / 30)); // or your previous formula
        }

        if (score > 0) {
            int newTotal = currentUser.getPoints() + score;
            currentUser.setPoints(newTotal);
            repository.updateUser(currentUser);
            binding.userPagePointsTextView.setText("Points: " + newTotal);
            Toast.makeText(this, "Correct! +" + score + " points", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect or time is up.", Toast.LENGTH_SHORT).show();
        }

        hasAnsweredThisChallenge = true;
        binding.userPageSubmitButton.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
