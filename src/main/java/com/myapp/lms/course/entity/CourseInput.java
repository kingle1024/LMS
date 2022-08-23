package com.myapp.lms.course.entity;

import lombok.Data;

@Data
public class CourseInput {
    long id;
    long categoryId;
    String subject;
    String keyword;
    String summary;
    String contents;
    long price;
    long salePrice;
    String saleEndDtText;

    // delete
    String idList;
}
