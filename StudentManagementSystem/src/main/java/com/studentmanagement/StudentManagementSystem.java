package com.studentmanagement;

import com.studentmanagement.model.Student;
import com.studentmanagement.service.StudentService;
import com.studentmanagement.util.InputValidator;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Main Application Class - Student Management System
 * Purpose: Provides console-based user interface for managing student records
 * Time Complexity: Menu operations are O(1), CRUD operations depend on StudentService
 * This is the entry point of the application that handles user interaction.
 * Implements menu-driven navigation with input validation and error handling.
 * Uses Scanner for user input and delegates business logic to StudentService.
 */
public class StudentManagementSystem {

    private StudentService studentService;
    private Scanner scanner;

    // Constructor - O(n) due to StudentService initialization
    // Initializes the service layer and scanner for user input
    public StudentManagementSystem() {
        this.studentService = new StudentService();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Main method - Application entry point
     * Time Complexity: Depends on user interactions
     */
    public static void main(String[] args) {
        StudentManagementSystem system = new StudentManagementSystem();
        system.run();
    }

    /**
     * Main application loop
     * Time Complexity: O(∞) - runs until user exits
     * Displays menu and processes user choices in a loop.
     * Handles invalid menu selections gracefully.
     */
    public void run() {
        System.out.println("=".repeat(60));
        System.out.println("    WELCOME TO STUDENT MANAGEMENT SYSTEM");
        System.out.println("=".repeat(60));

        while (true) {
            displayMenu();
            int choice = getMenuChoice();

            switch (choice) {
                case 1:
                    addNewStudent();
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    displayStatistics();
                    break;
                case 7:
                    System.out.println("\nThank you for using Student Management System!");
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("\n❌ Invalid choice! Please select 1-7.");
            }

            // Pause before showing menu again
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }


     // O(1)
    private void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("              MAIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. 📝 Add New Student");
        System.out.println("2. 👥 View All Students");
        System.out.println("3. 🔍 Search Student by ID");
        System.out.println("4. ✏️  Update Student Information");
        System.out.println("5. 🗑️  Delete Student");
        System.out.println("6. 📊 Display Statistics");
        System.out.println("7. 🚪 Exit");
        System.out.println("=".repeat(50));
        System.out.print("Enter your choice (1-7): ");
    }

    /**
     * Gets and validates menu choice from user
     * Time Complexity: O(1) per attempt
     * Handles invalid input gracefully and prompts user to retry.
     */
    private int getMenuChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            return choice;
        } catch (NumberFormatException e) {
            return -1; // Invalid choice
        }
    }

    /**
     * Adds a new student to the system
     * Time Complexity: O(n) due to StudentService.addStudent()
     * Collects student information with validation and adds to system.
     * Provides immediate feedback on success/failure.
     */
    private void addNewStudent() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("         ADD NEW STUDENT");
        System.out.println("=".repeat(40));

        try {
            // Auto-generate student ID
            int studentId = studentService.getNextStudentId();
            System.out.println("Student ID (auto-generated): " + studentId);

            // Get first name with validation
            String firstName = getValidatedInput("First Name: ", InputValidator::isValidName,
                    "First name must contain only letters and be at least 2 characters long.");

            // Get last name with validation
            String lastName = getValidatedInput("Last Name: ", InputValidator::isValidName,
                    "Last name must contain only letters and be at least 2 characters long.");

            // Get email with validation
            String email = getValidatedInput("Email: ", InputValidator::isValidEmail,
                    "Please enter a valid email address (e.g., student@example.com).");

            // Get phone number with validation
            String phoneNumber = getValidatedInput("Phone Number: ", InputValidator::isValidPhoneNumber,
                    "Phone number must be 10-15 digits (optional + prefix for international).");

            // Get course with validation
            String course = getValidatedInput("Course: ", InputValidator::isValidCourse,
                    "Course name must be at least 2 characters and contain only letters, numbers, and common punctuation.");

            // Get GPA with validation
            double gpa = getValidatedGpa();

            // Create and add student
            Student student = new Student(studentId, firstName, lastName, email, phoneNumber, course, gpa);

            if (studentService.addStudent(student)) {
                System.out.println("\n✅ Student added successfully!");
                System.out.println("Student Details:");
                System.out.println("-".repeat(80));
                System.out.println(student);
            } else {
                System.out.println("\n❌ Failed to add student. Please check the information and try again.");
            }

        } catch (Exception e) {
            System.out.println("\n❌ An error occurred while adding the student: " + e.getMessage());
        }
    }

    /**
     * Displays all students in formatted table
     * Time Complexity: O(n) where n is number of students
     * Shows all student records in a well-formatted console table.
     */
    private void viewAllStudents() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                                    ALL STUDENTS");
        System.out.println("=".repeat(100));

        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("📭 No students found in the system.");
            return;
        }

        // Display header
        System.out.println(String.format("%-5s | %-20s | %-25s | %-12s | %-15s | %s",
                "ID", "Name", "Email", "Phone", "Course", "GPA"));
        System.out.println("-".repeat(100));

        // Display all students
        for (Student student : students) {
            System.out.println(student);
        }

        System.out.println("-".repeat(100));
        System.out.println("Total Students: " + students.size());
    }

    /**
     * Searches for a student by ID
     * Time Complexity: O(n) due to linear search
     * Allows user to find and view a specific student's details.
     */
    private void searchStudent() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("        SEARCH STUDENT");
        System.out.println("=".repeat(40));

        try {
            System.out.print("Enter Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine().trim());

            if (!InputValidator.isValidStudentId(studentId)) {
                System.out.println("❌ Invalid Student ID. Please enter a positive number.");
                return;
            }

            Optional<Student> studentOpt = studentService.findStudentById(studentId);

            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                System.out.println("\n✅ Student Found:");
                System.out.println("-".repeat(80));
                System.out.println(String.format("%-5s | %-20s | %-25s | %-12s | %-15s | %s",
                        "ID", "Name", "Email", "Phone", "Course", "GPA"));
                System.out.println("-".repeat(80));
                System.out.println(student);
            } else {
                System.out.println("❌ Student with ID " + studentId + " not found.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a valid student ID.");
        }
    }

    /**
     * Updates an existing student's information
     * Time Complexity: O(n) due to find and update operations
     * Allows modification of student details with validation.
     */
    private void updateStudent() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("       UPDATE STUDENT");
        System.out.println("=".repeat(40));

        try {
            System.out.print("Enter Student ID to update: ");
            int studentId = Integer.parseInt(scanner.nextLine().trim());

            Optional<Student> studentOpt = studentService.findStudentById(studentId);

            if (!studentOpt.isPresent()) {
                System.out.println("❌ Student with ID " + studentId + " not found.");
                return;
            }

            Student existingStudent = studentOpt.get();
            System.out.println("\nCurrent Student Information:");
            System.out.println("-".repeat(80));
            System.out.println(existingStudent);

            System.out.println("\nEnter new information (press Enter to keep current value):");

            // Update fields with validation
            String firstName = getOptionalInput("First Name [" + existingStudent.getFirstName() + "]: ",
                    existingStudent.getFirstName(), InputValidator::isValidName,
                    "First name must contain only letters and be at least 2 characters long.");

            String lastName = getOptionalInput("Last Name [" + existingStudent.getLastName() + "]: ",
                    existingStudent.getLastName(), InputValidator::isValidName,
                    "Last name must contain only letters and be at least 2 characters long.");

            String email = getOptionalInput("Email [" + existingStudent.getEmail() + "]: ",
                    existingStudent.getEmail(), InputValidator::isValidEmail,
                    "Please enter a valid email address.");

            String phoneNumber = getOptionalInput("Phone Number [" + existingStudent.getPhoneNumber() + "]: ",
                    existingStudent.getPhoneNumber(), InputValidator::isValidPhoneNumber,
                    "Phone number must be 10-15 digits.");

            String course = getOptionalInput("Course [" + existingStudent.getCourse() + "]: ",
                    existingStudent.getCourse(), InputValidator::isValidCourse,
                    "Course name must be at least 2 characters.");

            double gpa = getOptionalGpa(existingStudent.getGpa());

            // Create updated student
            Student updatedStudent = new Student(studentId, firstName, lastName, email, phoneNumber, course, gpa);

            if (studentService.updateStudent(updatedStudent)) {
                System.out.println("\n✅ Student updated successfully!");
                System.out.println("Updated Student Details:");
                System.out.println("-".repeat(80));
                System.out.println(updatedStudent);
            } else {
                System.out.println("\n❌ Failed to update student.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a valid student ID.");
        }
    }

    /**
     * Deletes a student from the system
     * Time Complexity: O(n) due to find and delete operations
     * Removes student after confirmation to prevent accidental deletion.
     */
    private void deleteStudent() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("       DELETE STUDENT");
        System.out.println("=".repeat(40));

        try {
            System.out.print("Enter Student ID to delete: ");
            int studentId = Integer.parseInt(scanner.nextLine().trim());

            Optional<Student> studentOpt = studentService.findStudentById(studentId);

            if (!studentOpt.isPresent()) {
                System.out.println("❌ Student with ID " + studentId + " not found.");
                return;
            }

            Student student = studentOpt.get();
            System.out.println("\nStudent to be deleted:");
            System.out.println("-".repeat(80));
            System.out.println(student);

            System.out.print("\n⚠️  Are you sure you want to delete this student? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("yes") || confirmation.equals("y")) {
                if (studentService.deleteStudent(studentId)) {
                    System.out.println("\n✅ Student deleted successfully!");
                } else {
                    System.out.println("\n❌ Failed to delete student.");
                }
            } else {
                System.out.println("\n❌ Deletion cancelled.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a valid student ID.");
        }
    }

    /**
     * Displays system statistics
     * Time Complexity: O(n) to calculate statistics
     * Shows useful information about the student database.
     */
    private void displayStatistics() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            SYSTEM STATISTICS");
        System.out.println("=".repeat(50));

        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("📭 No students in the system.");
            return;
        }

        // Calculate statistics
        int totalStudents = students.size();
        double totalGpa = students.stream().mapToDouble(Student::getGpa).sum();
        double averageGpa = totalGpa / totalStudents;
        double highestGpa = students.stream().mapToDouble(Student::getGpa).max().orElse(0.0);
        double lowestGpa = students.stream().mapToDouble(Student::getGpa).min().orElse(0.0);

        // Count students by GPA ranges
        // Count students by GPA ranges
        long excellentStudents = students.stream().filter(s -> s.getGpa() >= 8.0).count();
        long goodStudents = students.stream().filter(s -> s.getGpa() >= 6.0 && s.getGpa() < 8.0).count();
        long averageStudents = students.stream().filter(s -> s.getGpa() >= 4.0 && s.getGpa() < 6.0).count();
        long failStudents = students.stream().filter(s -> s.getGpa() < 4.0).count();

        System.out.println("GPA Distribution:");
        System.out.println("  🌟 Excellent (8.0-10.0): " + excellentStudents + " students");
        System.out.println("  👍 Good (6.0-7.9): " + goodStudents + " students");
        System.out.println("  📊 Average (4.0-5.9): " + averageStudents + " students");
        System.out.println("  ❌ Fail (<4.0): " + failStudents + " students");
    }


        // Helper methods for input validation and handling

    /**
     * Gets validated input from user with retry mechanism
     * Time Complexity: O(1) per attempt
     */
    private String getValidatedInput(String prompt, java.util.function.Predicate<String> validator, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (validator.test(input)) {
                return input;
            } else {
                System.out.println("❌ " + errorMessage);
            }
        }
    }

    /**
     * Gets optional input (allows keeping existing value)
     * Time Complexity: O(1) per attempt
     */
    private String getOptionalInput(String prompt, String currentValue,
                                    java.util.function.Predicate<String> validator, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return currentValue; // Keep existing value
            }

            if (validator.test(input)) {
                return input;
            } else {
                System.out.println("❌ " + errorMessage);
            }
        }
    }

    /**
     * Gets validated GPA input
     * Time Complexity: O(1) per attempt
     */
    private double getValidatedGpa() {
        while (true) {
            System.out.print("GPA (0.0-4.0): ");
            try {
                double gpa = Double.parseDouble(scanner.nextLine().trim());
                if (InputValidator.isValidGpa(gpa)) {
                    return gpa;
                } else {
                    System.out.println("❌ GPA must be between 0.0 and 4.0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number for GPA.");
            }
        }
    }

    /**
     * Gets optional GPA input (allows keeping existing value)
     * Time Complexity: O(1) per attempt
     */
    private double getOptionalGpa(double currentGpa) {
        while (true) {
            System.out.print("GPA [" + String.format("%.2f", currentGpa) + "]: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return currentGpa; // Keep existing value
            }

            try {
                double gpa = Double.parseDouble(input);
                if (InputValidator.isValidGpa(gpa)) {
                    return gpa;
                } else {
                    System.out.println("❌ GPA must be between 0.0 and 4.0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number for GPA.");
            }
        }
    }
}