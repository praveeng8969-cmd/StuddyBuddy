package com.studybuddy.service;

import com.studybuddy.entity.Membership;
import com.studybuddy.entity.Student;
import com.studybuddy.entity.StudyGroup;
import com.studybuddy.exception.BusinessRuleException;
import com.studybuddy.exception.ResourceNotFoundException;
import com.studybuddy.repository.MembershipRepository;
import com.studybuddy.repository.StudentRepository;
import com.studybuddy.repository.StudyGroupRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MembershipService
{
    private final MembershipRepository membershipRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final StudentRepository studentRepository;

    public MembershipService(MembershipRepository membershipRepository,
                             StudyGroupRepository studyGroupRepository,
                             StudentRepository studentRepository)
    {
        this.membershipRepository = membershipRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.studentRepository = studentRepository;
    }

    // Join a study group with capacity and duplicate checks
    @Transactional
    public Membership joinGroup(Long groupId, Long studentId)
    {
        // 1. Find StudyGroup
        StudyGroup studyGroup = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Study group not found"));

        // 2. Find Student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // 3. Duplicate membership check
        if (membershipRepository.existsByStudyGroupIdAndStudentId(groupId, studentId))
        {
            throw new BusinessRuleException("Student is already a member of this group");
        }

        // 4. Maximum group capacity check
        long currentMembers = membershipRepository.countByStudyGroupId(groupId);
        if (currentMembers >= studyGroup.getMaxMembers())
        {
            throw new BusinessRuleException("Study group is full");
        }

        // 5. Create and save membership
        Membership membership = new Membership(studyGroup, student, LocalDateTime.now());
        return membershipRepository.save(membership);
    }

    // Leave a study group
    @Transactional
    public void leaveGroup(Long groupId, Long studentId)
    {
        Membership membership = membershipRepository.findByStudyGroupIdAndStudentId(groupId, studentId)
                .orElseThrow(() -> new BusinessRuleException("Student is not a member of this group"));

        membershipRepository.delete(membership);
    }

    // Remove member by creator only
    @Transactional
    public void removeMember(Long groupId, Long requesterId, Long memberStudentId)
    {
        // 1. Find StudyGroup
        StudyGroup studyGroup = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Study group not found"));

        // 2. Find requester Student
        studentRepository.findById(requesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // 3. Verify requester is the group creator
        if (!studyGroup.getCreator().getId().equals(requesterId))
        {
            throw new BusinessRuleException("Only the group creator can remove members");
        }

        // 4. Find member to remove
        Membership membership = membershipRepository.findByStudyGroupIdAndStudentId(groupId, memberStudentId)
                .orElseThrow(() -> new BusinessRuleException("Student is not a member of this group"));

        membershipRepository.delete(membership);
    }

    public List<Membership> getMembershipsByGroupId(Long groupId)
    {
        return membershipRepository.findByStudyGroupId(groupId);
    }

    public long getMemberCountByGroupId(Long groupId)
    {
        return membershipRepository.countByStudyGroupId(groupId);
    }

    public boolean isMember(Long groupId, Long studentId)
    {
        return membershipRepository.existsByStudyGroupIdAndStudentId(groupId, studentId);
    }
}
