package com.studybuddy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "study_group")
public class StudyGroup
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Group name must not be blank")
    private String groupName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private Student creator;

    @NotNull(message = "Maximum members limit is required")
    @Min(value = 1, message = "Maximum members must be positive")
    private Integer maxMembers;

    @Column(length = 1000)
    private String description;

    private LocalDate createdDate;

    public StudyGroup()
    {
    }

    public StudyGroup(String groupName, Subject subject, Student creator, Integer maxMembers, String description)
    {
        this.groupName = groupName;
        this.subject = subject;
        this.creator = creator;
        this.maxMembers = maxMembers;
        this.description = description;
        this.createdDate = LocalDate.now();
    }

    @PrePersist
    public void onCreate()
    {
        if (this.createdDate == null)
        {
            this.createdDate = LocalDate.now();
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

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public Subject getSubject()
    {
        return subject;
    }

    public void setSubject(Subject subject)
    {
        this.subject = subject;
    }

    public Student getCreator()
    {
        return creator;
    }

    public void setCreator(Student creator)
    {
        this.creator = creator;
    }

    public Integer getMaxMembers()
    {
        return maxMembers;
    }

    public void setMaxMembers(Integer maxMembers)
    {
        this.maxMembers = maxMembers;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public LocalDate getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate)
    {
        this.createdDate = createdDate;
    }
}
