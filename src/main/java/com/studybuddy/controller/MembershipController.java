package com.studybuddy.controller;

import com.studybuddy.exception.BusinessRuleException;
import com.studybuddy.exception.ResourceNotFoundException;
import com.studybuddy.service.MembershipService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MembershipController
{
    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService)
    {
        this.membershipService = membershipService;
    }

    @PostMapping("/groups/{groupId}/join")
    public String joinGroup(@PathVariable Long groupId,
                            @RequestParam Long studentId,
                            RedirectAttributes redirectAttributes)
    {
        try
        {
            membershipService.joinGroup(groupId, studentId);
            redirectAttributes.addFlashAttribute("successMessage", "Student joined the group successfully");
        }
        catch (BusinessRuleException | ResourceNotFoundException ex)
        {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/groups/" + groupId;
    }

    @PostMapping("/groups/{groupId}/leave")
    public String leaveGroup(@PathVariable Long groupId,
                             @RequestParam Long studentId,
                             RedirectAttributes redirectAttributes)
    {
        try
        {
            membershipService.leaveGroup(groupId, studentId);
            redirectAttributes.addFlashAttribute("successMessage", "Student left the group successfully");
        }
        catch (BusinessRuleException | ResourceNotFoundException ex)
        {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/groups/" + groupId;
    }

    @PostMapping("/groups/{groupId}/remove")
    public String removeMember(@PathVariable Long groupId,
                               @RequestParam Long requesterId,
                               @RequestParam Long memberStudentId,
                               RedirectAttributes redirectAttributes)
    {
        try
        {
            membershipService.removeMember(groupId, requesterId, memberStudentId);
            redirectAttributes.addFlashAttribute("successMessage", "Member removed successfully");
        }
        catch (BusinessRuleException | ResourceNotFoundException ex)
        {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/groups/" + groupId;
    }
}
