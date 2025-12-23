package com.studentmanagement.model;

/**
 * Student Model Class
 * Purpose: Represents a student entity with basic information and validation
 * Time Complexity: All operations are O(1) - constant time for getters/setters
 * This class encapsulates student data and provides validation methods.
 * Uses standard Java Bean pattern for data encapsulation.
 */
public class Student {
    private int studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String course;
    private double gpa;

    //Default constructor - o(1)
    public Student() {}

    // Parameterized constructor - O(1)
    // Used for creating student object with all required fields;
    public Student(int studentId,String firstName,String lastName,String email,
                   String phoneNumber,String course,double gpa){
        this.studentId=studentId;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.course=course;
        this.gpa=gpa;
    }

    // Getters and Setters - All O(1) operations
    // These provide controlled access to private fields

    public int getStudentId(){
        return studentId;
    }
    public void setStudentId(int studentId){
        this.studentId =studentId;
    }
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName=firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName=lastName;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }
    public String getCourse(){
        return course;
    }
    public void setCourse(String course){
        this.course=course;
    }
    public double getGpa(){
        return gpa;
    }
    public void setGpa(double gpa){
        this.gpa=gpa;
    }

    // Full name getter - O(1)
    // Convenience method to get complete name

    public String getFullName(){
        return firstName + " "+ lastName;
    }

    // toString method for formatted display - O(1)
    // Used for console output formatting

    @Override
    public String toString() {
        return String.format("ID: %-5d | Name: %-20s | Email: %-25s | Phone: %-12s | Course: %-15s | GPA: %.2f",
                studentId, getFullName(), email, phoneNumber, course, gpa);
    }

    // equals method for object comparison - O(1)
    // Used for finding students by ID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentId == student.studentId;
    }

    // hashCode method - O(1)
    // Required when overriding equals, uses studentId as unique identifier
    @Override
    public int hashCode() {
        return Integer.hashCode(studentId);
    }
}

