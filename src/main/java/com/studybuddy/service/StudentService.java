package com.studybuddy.service;

import com.studybuddy.entity.Student;
import com.studybuddy.exception.ResourceNotFoundException;
import com.studybuddy.repository.StudentRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StudentService
{
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository)
    {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student)
    {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents()
    {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id)
    {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
    }

    public long getStudentCount()
    {
        return studentRepository.count();
    }
}
