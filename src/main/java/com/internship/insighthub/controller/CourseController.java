package com.internship.insighthub.controller;

import com.internship.insighthub.dto.CourseDto;
import com.internship.insighthub.dto.SectionDto;
import com.internship.insighthub.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // GET /api/courses  -> список курсов
    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    // GET /api/courses/{courseId}/sections -> секции курса
    @GetMapping("/{courseId}/sections")
    public ResponseEntity<List<SectionDto>> getSectionsByCourse(
            @PathVariable Long courseId
    ) {
        List<SectionDto> sections = courseService.getSectionsByCourseId(courseId);
        return ResponseEntity.ok(sections);
    }
}
