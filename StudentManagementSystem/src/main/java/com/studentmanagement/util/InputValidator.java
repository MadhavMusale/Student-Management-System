package com.studentmanagement.util;

import java.util.regex.Pattern;

/**
 * Input Validation Utility Class
 * Purpose: Provides static methods for validating user input data
 * Time Complexity: All validation methods are O(1) - constant time operations
 * This class contains validation logic for different types of student data.
 * Uses regex patterns for email and phone validation for efficiency.
 */
public class InputValidator {
    // Regex patterns for validation - compiled once for efficiency
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[+]?[0-9]{10,15}$");

    /**
     * Validates if a string is not null and not empty after trimming
     * Time Complexity: O(1)
     * Used for validating names and course fields
     */
    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Validates student ID - must be positive integer
     * Time Complexity: O(1)
     * Student IDs should be positive numbers only
     */
    public static boolean isValidStudentId(int studentId) {
        return studentId > 0;
    }

    /**
     * Validates email format using regex pattern
     * Time Complexity: O(1) - regex pattern matching
     * Ensures email follows standard format: user@domain.extension
     */
    public static boolean isValidEmail(String email) {
        if (!isValidString(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates phone number format
     * Time Complexity: O(1) - regex pattern matching
     * Accepts 10-15 digits with optional + prefix for international numbers
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (!isValidString(phoneNumber)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber.trim()).matches();
    }

    /**
     * Validates GPA range (0.0 to 10.0)
     * Time Complexity: O(1)
     * Standard GPA scale validation
     */
    public static boolean isValidGpa(double gpa) {
        return gpa >= 0.0 && gpa <= 10.0;
    }

    /**
     * Validates name fields (first name, last name)
     * Time Complexity: O(1)
     * Names should contain only letters and spaces, minimum 2 characters
     */
    public static boolean isValidName(String name) {
        if (!isValidString(name)) {
            return false;
        }
        String trimmedName = name.trim();
        return trimmedName.length() >= 2 &&
                trimmedName.matches("^[a-zA-Z\\s]+$");
    }

    /**
     * Validates course name
     * Time Complexity: O(1)
     * Course names can contain letters, numbers, spaces, and common punctuation
     */
    public static boolean isValidCourse(String course) {
        if (!isValidString(course)) {
            return false;
        }
        String trimmedCourse = course.trim();
        return trimmedCourse.length() >= 2 &&
                trimmedCourse.matches("^[a-zA-Z0-9\\s&.-]+$");
    }
}
