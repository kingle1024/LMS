package com.myapp.lms.course.service;

import com.myapp.lms.course.dto.CourseDto;
import com.myapp.lms.course.entity.Course;
import com.myapp.lms.course.entity.CourseInput;
import com.myapp.lms.course.model.CourseParam;

import java.util.List;

public interface CourseService{

    /**
     * 강좌 등록
     * @param subject
     * @return
     */
    boolean add(CourseInput parameter);

    /**
     * 강좌 목록
     * @param parameter
     * @return
     */
    List<CourseDto> list(CourseParam parameter);

    boolean del(CourseInput parameter);

    /**
     * 강좌 상세 정보
     * @param id
     * @return
     */
    CourseDto getById(long id);

    /**
     * 강좌 정보 수정
     * @param parameter
     * @return
     */
    boolean set(CourseInput parameter);
}
