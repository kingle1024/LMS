package com.myapp.lms.course.service;

import com.myapp.lms.course.dto.CourseDto;
import com.myapp.lms.course.model.CourseInput;
import com.myapp.lms.course.model.CourseParam;
import com.myapp.lms.course.model.ServiceResult;
import com.myapp.lms.course.model.TakeCourseInput;

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

    boolean del(String idList);

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

    /**
     * 프론트 강좌 목록
     * @param parameter
     * @return
     */
    List<CourseDto> frontList(CourseParam parameter);

    /**
     * 프론트 강좌 상세 정보
     * @param courseId
     * @return
     */
    CourseDto frontDetail(long courseId);

    ServiceResult req(TakeCourseInput parameter);

    /**
     * 강좌 전체 목록
     * @return
     */
    List<CourseDto> listAll();
}
