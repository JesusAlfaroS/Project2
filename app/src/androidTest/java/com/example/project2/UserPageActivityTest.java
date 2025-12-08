package com.example.project2;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserPageActivityTest {

    //Check if there are any issues when executing
    @Test
    public void testLaunchActivity_noUserId_stillLaunches() {

        Intent intent = new Intent(
                ApplicationProvider.getApplicationContext(),
                UserPageActivity.class
        );

        ActivityScenario<UserPageActivity> scenario =
                ActivityScenario.launch(intent);

        scenario.close();
    }
}
