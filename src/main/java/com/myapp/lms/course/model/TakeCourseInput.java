package com.myapp.lms.course.model;

import lombok.Data;

@Data
public class TakeCourseInput {
    long courseId;
    String userId;
}
