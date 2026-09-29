package com.studybuddy.service;

import com.studybuddy.entity.StudyGroup;
import com.studybuddy.exception.ResourceNotFoundException;
import com.studybuddy.repository.StudyGroupRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StudyGroupService
{
    private final StudyGroupRepository studyGroupRepository;

    public StudyGroupService(StudyGroupRepository studyGroupRepository)
    {
        this.studyGroupRepository = studyGroupRepository;
    }

    public StudyGroup createStudyGroup(StudyGroup studyGroup)
    {
        return studyGroupRepository.save(studyGroup);
    }

    public List<StudyGroup> getAllStudyGroups()
    {
        return studyGroupRepository.findAll();
    }

    public StudyGroup getStudyGroupById(Long id)
    {
        return studyGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Study group not found"));
    }

    public List<StudyGroup> getStudyGroupsBySubjectId(Long subjectId)
    {
        return studyGroupRepository.findBySubjectId(subjectId);
    }

    public long getStudyGroupCount()
    {
        return studyGroupRepository.count();
    }
}
