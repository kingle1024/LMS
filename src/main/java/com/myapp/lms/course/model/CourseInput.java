package com.myapp.lms.course.model;

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
    String fileName;
    String urlFileName;
}
