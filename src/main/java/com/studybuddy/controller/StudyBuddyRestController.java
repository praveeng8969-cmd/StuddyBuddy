package com.studybuddy.controller;

import com.studybuddy.entity.Student;
import com.studybuddy.entity.StudyGroup;
import com.studybuddy.entity.Subject;
import com.studybuddy.service.MembershipService;
import com.studybuddy.service.StudentService;
import com.studybuddy.service.StudyGroupService;
import com.studybuddy.service.SubjectService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StudyBuddyRestController
{
    private final StudyGroupService studyGroupService;
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final MembershipService membershipService;

    public StudyBuddyRestController(StudyGroupService studyGroupService,
                                   SubjectService subjectService,
                                   StudentService studentService,
                                   MembershipService membershipService)
    {
        this.studyGroupService = studyGroupService;
        this.subjectService = subjectService;
        this.studentService = studentService;
        this.membershipService = membershipService;
    }

    // 1. Create StudyGroup
    @PostMapping("/groups")
    public ResponseEntity<StudyGroup> createGroup(@RequestBody CreateGroupRequest request)
    {
        Subject subject = subjectService.getSubjectById(request.getSubjectId());
        Student creator = studentService.getStudentById(request.getCreatorId());

        StudyGroup group = new StudyGroup(
                request.getGroupName(),
                subject,
                creator,
                request.getMaxMembers(),
                request.getDescription()
        );

        StudyGroup saved = studyGroupService.createStudyGroup(group);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // 2. Join StudyGroup
    @PostMapping("/groups/{groupId}/join/{studentId}")
    public ResponseEntity<Map<String, String>> joinGroup(@PathVariable Long groupId,
                                                         @PathVariable Long studentId)
    {
        membershipService.joinGroup(groupId, studentId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student joined the group successfully");
        return ResponseEntity.ok(response);
    }

    // 3. Leave StudyGroup
    @DeleteMapping("/groups/{groupId}/leave/{studentId}")
    public ResponseEntity<Map<String, String>> leaveGroup(@PathVariable Long groupId,
                                                          @PathVariable Long studentId)
    {
        membershipService.leaveGroup(groupId, studentId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student left the group successfully");
        return ResponseEntity.ok(response);
    }

    // 4. Get StudyGroups by Subject
    @GetMapping("/groups/subject/{subjectId}")
    public ResponseEntity<List<StudyGroup>> getGroupsBySubject(@PathVariable Long subjectId)
    {
        List<StudyGroup> groups = studyGroupService.getStudyGroupsBySubjectId(subjectId);
        return ResponseEntity.ok(groups);
    }

    // 5. Remove Member (requesterId passed as query parameter)
    @DeleteMapping("/groups/{groupId}/members/{studentId}")
    public ResponseEntity<Map<String, String>> removeMember(@PathVariable Long groupId,
                                                            @PathVariable Long studentId,
                                                            @RequestParam Long requesterId)
    {
        membershipService.removeMember(groupId, requesterId, studentId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Member removed successfully");
        return ResponseEntity.ok(response);
    }

    // Simple static request object for REST creation
    public static class CreateGroupRequest
    {
        private String groupName;
        private Long subjectId;
        private Long creatorId;
        private Integer maxMembers;
        private String description;

        public CreateGroupRequest()
        {
        }

        public String getGroupName()
        {
            return groupName;
        }

        public void setGroupName(String groupName)
        {
            this.groupName = groupName;
        }

        public Long getSubjectId()
        {
            return subjectId;
        }

        public void setSubjectId(Long subjectId)
        {
            this.subjectId = subjectId;
        }

        public Long getCreatorId()
        {
            return creatorId;
        }

        public void setCreatorId(Long creatorId)
        {
            this.creatorId = creatorId;
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
    }
}
