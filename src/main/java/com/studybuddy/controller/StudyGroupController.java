package com.studybuddy.controller;

import com.studybuddy.entity.Membership;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudyGroupController
{
    private final StudyGroupService studyGroupService;
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final MembershipService membershipService;

    public StudyGroupController(StudyGroupService studyGroupService,
                                SubjectService subjectService,
                                StudentService studentService,
                                MembershipService membershipService)
    {
        this.studyGroupService = studyGroupService;
        this.subjectService = subjectService;
        this.studentService = studentService;
        this.membershipService = membershipService;
    }

    @GetMapping("/groups")
    public String listGroups(@RequestParam(required = false) Long subjectId, Model model)
    {
        List<StudyGroup> groups;
        if (subjectId != null && subjectId > 0)
        {
            groups = studyGroupService.getStudyGroupsBySubjectId(subjectId);
        }
        else
        {
            groups = studyGroupService.getAllStudyGroups();
        }

        // Calculate member count for each group
        Map<Long, Long> memberCounts = new HashMap<>();
        for (StudyGroup group : groups)
        {
            long count = membershipService.getMemberCountByGroupId(group.getId());
            memberCounts.put(group.getId(), count);
        }

        model.addAttribute("groups", groups);
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("selectedSubjectId", subjectId);
        model.addAttribute("memberCounts", memberCounts);

        return "groups";
    }

    @GetMapping("/groups/create")
    public String showCreateGroupForm(Model model)
    {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("students", studentService.getAllStudents());
        return "create-group";
    }

    @PostMapping("/groups/create")
    public String createGroup(@RequestParam String groupName,
                              @RequestParam Long subjectId,
                              @RequestParam Long creatorId,
                              @RequestParam Integer maxMembers,
                              @RequestParam(required = false) String description,
                              RedirectAttributes redirectAttributes)
    {
        if (groupName == null || groupName.trim().isEmpty())
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Group name must not be blank");
            return "redirect:/groups/create";
        }
        if (maxMembers == null || maxMembers < 1)
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Maximum members must be at least 1");
            return "redirect:/groups/create";
        }

        Subject subject = subjectService.getSubjectById(subjectId);
        Student creator = studentService.getStudentById(creatorId);

        StudyGroup group = new StudyGroup(groupName.trim(), subject, creator, maxMembers, description);
        studyGroupService.createStudyGroup(group);

        redirectAttributes.addFlashAttribute("successMessage", "Study group created successfully");
        return "redirect:/groups";
    }

    @GetMapping("/groups/{id}")
    public String groupDetails(@PathVariable Long id, Model model)
    {
        StudyGroup group = studyGroupService.getStudyGroupById(id);
        List<Membership> memberships = membershipService.getMembershipsByGroupId(id);
        long currentMembers = membershipService.getMemberCountByGroupId(id);
        List<Student> allStudents = studentService.getAllStudents();

        model.addAttribute("group", group);
        model.addAttribute("memberships", memberships);
        model.addAttribute("currentMembers", currentMembers);
        model.addAttribute("isFull", currentMembers >= group.getMaxMembers());
        model.addAttribute("allStudents", allStudents);

        return "group-details";
    }
}
