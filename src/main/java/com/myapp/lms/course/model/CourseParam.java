package com.myapp.lms.course.model;

import com.myapp.lms.admin.model.CommonParam;
import lombok.Data;

@Data
public class CourseParam extends CommonParam {
    long id;
    long categoryId;
}
