package com.internship.insighthub.service;

import com.internship.insighthub.dto.CourseDto;
import com.internship.insighthub.dto.SectionDto;
import com.internship.insighthub.entity.Course;
import com.internship.insighthub.entity.Section;
import com.internship.insighthub.repository.CourseRepository;
import com.internship.insighthub.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    @Override
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(course -> new CourseDto(
                        course.getId(),
                        course.getTitle(),
                        course.getDescription()
                ))
                .toList();
    }

    @Override
    public List<SectionDto> getSectionsByCourseId(Long courseId) {
        // Если курс не существует — 404
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Course with id " + courseId + " not found")
                );

        List<Section> sections = sectionRepository.findByCourseId(course.getId());

        return sections.stream()
                .map(section -> new SectionDto(
                        section.getId(),
                        section.getContent(),
                        course.getId()
                ))
                .toList();
    }
}
