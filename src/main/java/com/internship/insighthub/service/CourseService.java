package com.internship.insighthub.service;

import com.internship.insighthub.dto.CourseDto;
import com.internship.insighthub.dto.SectionDto;

import java.util.List;

public interface CourseService {

    List<CourseDto> getAllCourses();

    List<SectionDto> getSectionsByCourseId(Long courseId);
}
