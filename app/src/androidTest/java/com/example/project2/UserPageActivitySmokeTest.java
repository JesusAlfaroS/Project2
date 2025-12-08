package com.example.project2;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserPageActivitySmokeTest {

    // it'll pass if it works
    @Test
    public void testActivityLaunchesSuccessfully() {

        ActivityScenario<UserPageActivity> scenario =
                ActivityScenario.launch(UserPageActivity.class);

    }
}