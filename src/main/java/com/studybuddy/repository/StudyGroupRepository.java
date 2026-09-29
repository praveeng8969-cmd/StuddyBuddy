package com.studybuddy.repository;

import com.studybuddy.entity.StudyGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long>
{
    List<StudyGroup> findBySubjectId(Long subjectId);
}
