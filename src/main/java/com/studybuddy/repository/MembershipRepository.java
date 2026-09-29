package com.studybuddy.repository;

import com.studybuddy.entity.Membership;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long>
{
    boolean existsByStudyGroupIdAndStudentId(Long studyGroupId, Long studentId);

    long countByStudyGroupId(Long studyGroupId);

    Optional<Membership> findByStudyGroupIdAndStudentId(Long studyGroupId, Long studentId);

    List<Membership> findByStudyGroupId(Long studyGroupId);

    void deleteByStudyGroupIdAndStudentId(Long studyGroupId, Long studentId);
}
