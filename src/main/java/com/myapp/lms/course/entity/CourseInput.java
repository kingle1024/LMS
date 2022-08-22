package com.myapp.lms.course.entity;

import lombok.Data;

@Data
public class CourseInput {
    long id;
    long categoryId;
    String subject;
}
