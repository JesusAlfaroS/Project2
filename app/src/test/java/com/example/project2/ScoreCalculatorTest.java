package com.example.project2;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the scoring logic similar to UserPageActivity.
 */
public class ScoreCalculatorTest {

    private int calculateScore(long elapsedMs) {
        long CHALLENGE_WINDOW_MS = 30_000L;

        if (elapsedMs >= CHALLENGE_WINDOW_MS) return 0;

        return (int) Math.max(10, 1000 - (elapsedMs / 30));
    }

    @Test
    public void testScoreFastAnswer() {
        // 1 second after challenge posted
        long elapsedMs = 1000;
        int score = calculateScore(elapsedMs);
        assertTrue(score > 900);  // should be high
    }

    @Test
    public void testScoreLateAnswer() {
        // 29 seconds after challenge posted
        long elapsedMs = 29000;
        int score = calculateScore(elapsedMs);
        assertTrue(score <= 50);  // should be nearly zero
    }
}
