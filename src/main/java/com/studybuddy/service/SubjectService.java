package com.studybuddy.service;

import com.studybuddy.entity.Subject;
import com.studybuddy.exception.ResourceNotFoundException;
import com.studybuddy.repository.SubjectRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SubjectService
{
    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository)
    {
        this.subjectRepository = subjectRepository;
    }

    public Subject saveSubject(Subject subject)
    {
        return subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects()
    {
        return subjectRepository.findAll();
    }

    public Subject getSubjectById(Long id)
    {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
    }

    public long getSubjectCount()
    {
        return subjectRepository.count();
    }
}
