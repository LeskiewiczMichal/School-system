package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DegreeService {

  DegreeDto getById(Long id);

  Page<DegreeDto> getDegrees(Pageable pageable);

  List<Degree> getByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy);

  DegreeDto createDegree(CreateDegreeRequest request);

  Page<CourseDto> getDegreeCourses(Long degreeId, Pageable pageable);

  Page<DegreeDto> search(String fieldOfStudy, Long facultyId, DegreeTitle title, Pageable pageable);

  void addImage(Long degreeId, MultipartFile image);
}
