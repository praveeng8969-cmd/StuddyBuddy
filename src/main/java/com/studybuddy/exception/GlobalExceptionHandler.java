package com.studybuddy.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(ResourceNotFoundException.class)
    public Object handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request, Model model)
    {
        if (isApiRequest(request))
        {
            Map<String, String> response = new HashMap<>();
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(BusinessRuleException.class)
    public Object handleBusinessRule(BusinessRuleException ex, HttpServletRequest request, Model model)
    {
        if (isApiRequest(request))
        {
            Map<String, String> response = new HashMap<>();
            response.put("error", ex.getMessage());
            // 409 Conflict for duplicate/full, or 400 for rule violation
            HttpStatus status = ex.getMessage().contains("already a member") || ex.getMessage().contains("full")
                    ? HttpStatus.CONFLICT
                    : HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(response, status);
        }

        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public Object handleGeneralException(Exception ex, HttpServletRequest request, Model model)
    {
        if (isApiRequest(request))
        {
            Map<String, String> response = new HashMap<>();
            response.put("error", ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return "error";
    }

    private boolean isApiRequest(HttpServletRequest request)
    {
        String uri = request.getRequestURI();
        return uri != null && uri.startsWith("/api");
    }
}
