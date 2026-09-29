package com.studybuddy.controller;

import com.studybuddy.entity.Student;
import com.studybuddy.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentController
{
    private final StudentService studentService;

    public StudentController(StudentService studentService)
    {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public String listStudents(Model model)
    {
        if (!model.containsAttribute("student"))
        {
            model.addAttribute("student", new Student());
        }
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    @PostMapping("/students")
    public String createStudent(@Valid @ModelAttribute("student") Student student,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("students", studentService.getAllStudents());
            return "students";
        }

        studentService.saveStudent(student);
        redirectAttributes.addFlashAttribute("successMessage", "Student added successfully");
        return "redirect:/students";
    }
}
