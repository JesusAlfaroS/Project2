package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "DAC_GYMLOG";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "login clicked", Toast.LENGTH_SHORT).show();
            }
        });

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "sign in clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
