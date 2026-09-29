package com.studybuddy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "membership")
public class Membership
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private LocalDateTime joinedAt;

    public Membership()
    {
    }

    public Membership(StudyGroup studyGroup, Student student)
    {
        this.studyGroup = studyGroup;
        this.student = student;
        this.joinedAt = LocalDateTime.now();
    }

    public Membership(StudyGroup studyGroup, Student student, LocalDateTime joinedAt)
    {
        this.studyGroup = studyGroup;
        this.student = student;
        this.joinedAt = joinedAt;
    }

    @PrePersist
    public void onJoin()
    {
        if (this.joinedAt == null)
        {
            this.joinedAt = LocalDateTime.now();
        }
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public StudyGroup getStudyGroup()
    {
        return studyGroup;
    }

    public void setStudyGroup(StudyGroup studyGroup)
    {
        this.studyGroup = studyGroup;
    }

    public Student getStudent()
    {
        return student;
    }

    public void setStudent(Student student)
    {
        this.student = student;
    }

    public LocalDateTime getJoinedAt()
    {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt)
    {
        this.joinedAt = joinedAt;
    }
}
