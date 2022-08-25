package com.myapp.lms.course.mapper;

import com.myapp.lms.course.dto.CourseDto;
import com.myapp.lms.course.dto.TakeCourseDto;
import com.myapp.lms.course.model.CourseParam;
import com.myapp.lms.course.model.TakeCourseParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TakeCourseMapper {
    long selectListCount(TakeCourseParam takeCourseParam);
    List<TakeCourseDto> selectList(TakeCourseParam parameter);
    // 페이징을 진행할 때에 보통 이렇게 2개를 받는다.
}
