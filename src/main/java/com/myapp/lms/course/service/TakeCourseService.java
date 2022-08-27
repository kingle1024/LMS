package com.myapp.lms.course.service;

import com.myapp.lms.course.dto.CourseDto;
import com.myapp.lms.course.dto.TakeCourseDto;
import com.myapp.lms.course.entity.TakeCourse;
import com.myapp.lms.course.model.*;

import java.util.List;

public interface TakeCourseService {
    /**
     * 수강 목록
     * @param parameter
     * @return
     */
    List<TakeCourseDto> list(TakeCourseParam parameter);
    boolean status(TakeCourseParam parameter);

    ServiceResult updateStatus(long id, String status);

    /**
     * 강좌 정보 가져오기
     * @param id
     * @return
     */
    TakeCourseDto detail(long id);
    ServiceResult cancel(long id);
    /**
     * 수강 내역
     * @param userId
     * @return
     */
    List<TakeCourseDto> myCourse(String userId);
}
