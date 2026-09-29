package com.studybuddy.controller;

import com.studybuddy.entity.Subject;
import com.studybuddy.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SubjectController
{
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService)
    {
        this.subjectService = subjectService;
    }

    @GetMapping("/subjects")
    public String listSubjects(Model model)
    {
        if (!model.containsAttribute("subject"))
        {
            model.addAttribute("subject", new Subject());
        }
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "subjects";
    }

    @PostMapping("/subjects")
    public String createSubject(@Valid @ModelAttribute("subject") Subject subject,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return "subjects";
        }

        subjectService.saveSubject(subject);
        redirectAttributes.addFlashAttribute("successMessage", "Subject added successfully");
        return "redirect:/subjects";
    }
}
