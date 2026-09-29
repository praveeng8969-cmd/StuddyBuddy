package com.studybuddy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "student")
public class Student
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Student name must not be blank")
    private String studentName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotBlank(message = "Department must not be blank")
    private String department;

    @NotNull(message = "Year of study is required")
    @Min(value = 1, message = "Year of study should be positive")
    private Integer yearOfStudy;

    public Student()
    {
    }

    public Student(String studentName, String email, String department, Integer yearOfStudy)
    {
        this.studentName = studentName;
        this.email = email;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public Integer getYearOfStudy()
    {
        return yearOfStudy;
    }

    public void setYearOfStudy(Integer yearOfStudy)
    {
        this.yearOfStudy = yearOfStudy;
    }
}
