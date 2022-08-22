package com.myapp.lms.course.mapper;

import com.myapp.lms.course.dto.CourseDto;
import com.myapp.lms.course.model.CourseParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    long selectListCount(CourseParam courseParam);
    List<CourseDto> selectList(CourseParam parameter);
}
