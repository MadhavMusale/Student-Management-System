package com.studentmanagement.service;

import com.studentmanagement.model.Student;
import com.studentmanagement.util.InputValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Student Service Class
 * Purpose: Handles all CRUD operations and business logic for student management
 * Time Complexity Summary:
 * - Add Student: O(1) for ArrayList, O(n) for file write
 * - Find Student: O(n) - linear search through ArrayList
 * - Update Student: O(n) - find + update + file write
 * - Delete Student: O(n) - find + remove + file write
 * - Display All: O(n) - iterate through all students
 * This class manages the in-memory ArrayList and file persistence.
 * Uses ArrayList for fast access and file I/O for data persistence.
 */
public class StudentService {

    private List<Student> students;
    private static final String DATA_FILE = "students.txt";
    private int nextStudentId;

    // Constructor - O(n) where n is number of students in file
    // Initializes the service and loads existing data from file
    public StudentService() {
        this.students = new ArrayList<>();
        this.nextStudentId = 1;
        loadStudentsFromFile();
    }

    /**
     * Adds a new student to the system
     * Time Complexity: O(1) for ArrayList add, O(n) for file write
     * @param student The student object to add
     * @return true if student was added successfully, false otherwise
     */
    public boolean addStudent(Student student) {
        // Validate student data before adding
        if (!validateStudent(student)) {
            return false;
        }

        // Check if student ID already exists - O(n)
        if (findStudentById(student.getStudentId()).isPresent()) {
            return false; // Student ID already exists
        }

        // Add to ArrayList - O(1) amortized
        students.add(student);

        // Update next available ID
        if (student.getStudentId() >= nextStudentId) {
            nextStudentId = student.getStudentId() + 1;
        }

        // Save to file - O(n)
        saveStudentsToFile();
        return true;
    }

    /**
     * Finds a student by ID
     * Time Complexity: O(n) - linear search through ArrayList
     * @param studentId The ID to search for
     * @return Optional containing the student if found, empty otherwise
     */
    public Optional<Student> findStudentById(int studentId) {
        return students.stream()
                .filter(student -> student.getStudentId() == studentId)
                .findFirst();
    }

    /**
     * Updates an existing student's information
     * Time Complexity: O(n) - find student + file write
     * @param updatedStudent The student with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateStudent(Student updatedStudent) {
        if (!validateStudent(updatedStudent)) {
            return false;
        }

        // Find the student to update - O(n)
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId() == updatedStudent.getStudentId()) {
                students.set(i, updatedStudent); // O(1) - direct index access
                saveStudentsToFile(); // O(n)
                return true;
            }
        }
        return false; // Student not found
    }

    /**
     * Deletes a student by ID
     * Time Complexity: O(n) - find and remove from ArrayList + file write
     * @param studentId The ID of the student to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteStudent(int studentId) {
        // Use removeIf for efficient removal - O(n)
        boolean removed = students.removeIf(student -> student.getStudentId() == studentId);

        if (removed) {
            saveStudentsToFile(); // O(n)
        }

        return removed;
    }

    /**
     * Gets all students in the system
     * Time Complexity: O(1) - returns reference to ArrayList
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students); // Return copy to prevent external modification
    }

    /**
     * Gets the next available student ID
     * Time Complexity: O(1)
     * @return The next available student ID
     */
    public int getNextStudentId() {
        return nextStudentId;
    }

    /**
     * Validates student data using InputValidator
     * Time Complexity: O(1) - all validation methods are constant time
     * @param student The student to validate
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateStudent(Student student) {
        return student != null &&
                InputValidator.isValidStudentId(student.getStudentId()) &&
                InputValidator.isValidName(student.getFirstName()) &&
                InputValidator.isValidName(student.getLastName()) &&
                InputValidator.isValidEmail(student.getEmail()) &&
                InputValidator.isValidPhoneNumber(student.getPhoneNumber()) &&
                InputValidator.isValidCourse(student.getCourse()) &&
                InputValidator.isValidGpa(student.getGpa());
    }

    /**
     * Loads students from file into memory
     * Time Complexity: O(n) where n is number of students in file
     * This method reads the file line by line and reconstructs Student objects.
     * Uses try-with-resources for proper file handling.
     */
    private void loadStudentsFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return; // No existing data file
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student student = parseStudentFromLine(line);
                if (student != null) {
                    students.add(student);
                    // Update next ID tracker
                    if (student.getStudentId() >= nextStudentId) {
                        nextStudentId = student.getStudentId() + 1;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading students from file: " + e.getMessage());
        }
    }

    /**
     * Saves all students to file
     * Time Complexity: O(n) where n is number of students
     * This method writes all student data to file in a structured format.
     * Each student is written as a single line with pipe-separated values.
     */
    private void saveStudentsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Student student : students) {
                writer.println(formatStudentForFile(student));
            }
        } catch (IOException e) {
            System.err.println("Error saving students to file: " + e.getMessage());
        }
    }

    /**
     * Parses a student object from a file line
     * Time Complexity: O(1)
     * @param line The line from file containing student data
     * @return Student object or null if parsing fails
     */
    private Student parseStudentFromLine(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length == 7) {
                return new Student(
                        Integer.parseInt(parts[0].trim()),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim(),
                        parts[5].trim(),
                        Double.parseDouble(parts[6].trim())
                );
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing student data: " + line);
        }
        return null;
    }

    /**
     * Formats a student object for file storage
     * Time Complexity: O(1)
     * @param student The student to format
     * @return Formatted string for file storage
     */
    private String formatStudentForFile(Student student) {
        return String.format("%d|%s|%s|%s|%s|%s|%.2f",
                student.getStudentId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getPhoneNumber(),
                student.getCourse(),
                student.getGpa()
        );
    }
}