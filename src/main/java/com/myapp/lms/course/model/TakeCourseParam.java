package com.myapp.lms.course.model;

import com.myapp.lms.admin.model.CommonParam;
import lombok.Data;

@Data
public class TakeCourseParam extends CommonParam {
    long id;
    String status;

    String userId;
    long searchCourseId;
}
