package com.myapp.lms.admin.service;

import com.myapp.lms.admin.dto.BannerDto;
import com.myapp.lms.admin.model.BannerInput;
import com.myapp.lms.admin.model.BannerParam;
import com.myapp.lms.course.dto.CourseDto;
import com.myapp.lms.course.model.CourseInput;
import com.myapp.lms.course.model.CourseParam;
import com.myapp.lms.course.model.ServiceResult;
import com.myapp.lms.course.model.TakeCourseInput;

import java.util.List;

public interface BannerService {

    /**
     * 강좌 등록
     * @param subject
     * @return
     */
    boolean add(BannerInput parameter);

    /**
     * 강좌 목록
     * @param parameter
     * @return
     */
    List<BannerDto> list(BannerParam parameter);

    boolean del(String idList);

    /**
     * 강좌 상세 정보
     * @param id
     * @return
     */
    BannerDto getById(long id);

    /**
     * 강좌 정보 수정
     * @param parameter
     * @return
     */
    boolean set(BannerInput parameter);

    /**
     * 프론트 강좌 목록
     * @param parameter
     * @return
     */
    List<BannerDto> frontList();

    /**
     * 프론트 강좌 상세 정보
     * @param courseId
     * @return
     */
    BannerDto frontDetail(long courseId);

    ServiceResult req(TakeCourseInput parameter);

    /**
     * 강좌 전체 목록
     * @return
     */
    List<BannerDto> listAll();
}
