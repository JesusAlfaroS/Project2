package com.example.project2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.database.RandomlyRepository;
import com.example.project2.database.entities.Challenge;

public class CreateChallengeActivity extends AppCompatActivity {

    private EditText editQuestion;
    private EditText editAnswer;
    private Button buttonSendChallenge;

    private RandomlyRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);

        // find views
        editQuestion = findViewById(R.id.edit_question);
        editAnswer = findViewById(R.id.edit_answer);
        buttonSendChallenge = findViewById(R.id.button_send_challenge);

        // repository
        repository = RandomlyRepository.getRepository(getApplication());

        buttonSendChallenge.setOnClickListener(v -> sendChallenge());
    }

    private void sendChallenge() {
        String question = editQuestion.getText().toString().trim();
        String answerStr = editAnswer.getText().toString().trim();

        if (question.isEmpty()) {
            Toast.makeText(this, "Please type the math problem", Toast.LENGTH_SHORT).show();
            return;
        }

        if (answerStr.isEmpty()) {
            Toast.makeText(this, "Please type the correct answer", Toast.LENGTH_SHORT).show();
            return;
        }

        double correctAnswer;
        try {
            correctAnswer = Double.parseDouble(answerStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Answer must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        long now = System.currentTimeMillis();

        // build Challenge entity
        Challenge challenge = new Challenge(question, correctAnswer, now);

        // save to Room via repository
        if (repository != null) {
            repository.insertChallenge(challenge);
            Toast.makeText(this, "Challenge saved!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Repository not available", Toast.LENGTH_SHORT).show();
        }

        // clear + close
        editQuestion.setText("");
        editAnswer.setText("");
        finish();   // go back to previous screen
    }
}
