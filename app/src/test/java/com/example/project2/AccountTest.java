package com.example.project2;

import static org.junit.Assert.assertNotEquals;

import com.example.project2.database.entities.User;

import junit.framework.TestCase;

import org.junit.Test;

/*
 * Two tests created by Jesus Alfaro-Suarez
 *
 * Tests the account creation process in Login & Signup Activity
 */
public class AccountTest extends TestCase {

    // Tests username when creating account
    @Test
    public void testUsername(){
        User user = new User("Anakin","DeathStar123");
        assertEquals("Anakin", user.getUsername()); //Should be True
        assertNotEquals("Luffy", user.getUsername()); //Should be False
    }

    // Tests password when creating account
    @Test
    public void testPassword(){
        User user = new User("Spongebob","SuperCoolPassword");
        assertEquals("SuperCoolPassword", user.getPassword()); //Should be True
        assertNotEquals("SpoilerItsNotGonnaEqual", user.getPassword()); //Should be False
    }
}