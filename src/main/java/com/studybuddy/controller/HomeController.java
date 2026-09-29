package com.studybuddy.controller;

import com.studybuddy.entity.StudyGroup;
import com.studybuddy.service.MembershipService;
import com.studybuddy.service.StudentService;
import com.studybuddy.service.StudyGroupService;
import com.studybuddy.service.SubjectService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{
    private final StudentService studentService;
    private final SubjectService subjectService;
    private final StudyGroupService studyGroupService;
    private final MembershipService membershipService;

    public HomeController(StudentService studentService,
                          SubjectService subjectService,
                          StudyGroupService studyGroupService,
                          MembershipService membershipService)
    {
        this.studentService = studentService;
        this.subjectService = subjectService;
        this.studyGroupService = studyGroupService;
        this.membershipService = membershipService;
    }

    @GetMapping("/")
    public String home(Model model)
    {
        long studentCount = studentService.getStudentCount();
        long subjectCount = subjectService.getSubjectCount();
        long groupCount = studyGroupService.getStudyGroupCount();
        List<StudyGroup> groups = studyGroupService.getAllStudyGroups();

        // Calculate member count for each group
        Map<Long, Long> memberCounts = new HashMap<>();
        for (StudyGroup group : groups)
        {
            long count = membershipService.getMemberCountByGroupId(group.getId());
            memberCounts.put(group.getId(), count);
        }

        model.addAttribute("studentCount", studentCount);
        model.addAttribute("subjectCount", subjectCount);
        model.addAttribute("groupCount", groupCount);
        model.addAttribute("groups", groups);
        model.addAttribute("memberCounts", memberCounts);

        return "index";
    }
}
