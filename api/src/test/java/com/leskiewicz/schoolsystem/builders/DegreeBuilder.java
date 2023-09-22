package com.leskiewicz.schoolsystem.builders;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.utils.Language;
import jakarta.persistence.*;
import java.util.List;

public class DegreeBuilder {

    private Long id;
    private DegreeTitle title;
    private String fieldOfStudy;
    private Double lengthOfStudy;
    private Double tuitionFeePerYear;
    private List<Language> language;
    private String description;
    private String imageName;
    private List<Course> courses;
    private Faculty faculty;

    public static DegreeBuilder aDegree() {
        return new DegreeBuilder();
    }

    public DegreeBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public DegreeBuilder title(DegreeTitle title) {
        this.title = title;
        return this;
    }

    public DegreeBuilder fieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
        return this;
    }

    public DegreeBuilder lengthOfStudy(Double lengthOfStudy) {
        this.lengthOfStudy = lengthOfStudy;
        return this;
    }

    public DegreeBuilder tuitionFeePerYear(Double tuitionFeePerYear) {
        this.tuitionFeePerYear = tuitionFeePerYear;
        return this;
    }

    public DegreeBuilder language(List<Language> language) {
        this.language = language;
        return this;
    }

    public DegreeBuilder description(String description) {
        this.description = description;
        return this;
    }

    public DegreeBuilder imageName(String imageName) {
        this.imageName = imageName;
        return this;
    }

    public DegreeBuilder courses(List<Course> courses) {
        this.courses = courses;
        return this;
    }

public DegreeBuilder faculty(Faculty faculty) {
        this.faculty = faculty;
        return this;
    }

    public Degree build() {
        return Degree.builder()
                .id(id)
                .title(title)
                .fieldOfStudy(fieldOfStudy)
                .lengthOfStudy(lengthOfStudy)
                .tuitionFeePerYear(tuitionFeePerYear)
                .language(language)
                .description(description)
                .imageName(imageName)
                .courses(courses)
                .faculty(faculty)
                .build();
    }
}
