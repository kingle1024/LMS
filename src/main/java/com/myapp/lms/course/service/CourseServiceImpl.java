package com.myapp.lms.course.service;

import com.myapp.lms.course.dto.CourseDto;
import com.myapp.lms.course.entity.Course;
import com.myapp.lms.course.entity.CourseInput;
import com.myapp.lms.course.mapper.CourseMapper;
import com.myapp.lms.course.model.CourseParam;
import com.myapp.lms.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public boolean add(CourseInput parameter) {
        Course course = Course.builder()
                        .categoryId(parameter.getCategoryId())
                        .subject(parameter.getSubject())
                        .regDt(LocalDateTime.now())
                        .build();

        courseRepository.save(course);
        return true;
    }

    @Override
    public List<CourseDto> list(CourseParam parameter) {
        long totalCount = courseMapper.selectListCount(parameter);
        List<CourseDto> list = courseMapper.selectList(parameter);
        if(!CollectionUtils.isEmpty(list)){
            int i = 0;
            for(CourseDto x : list){
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }
        return list;
    }

    @Override
    public boolean del(CourseInput parameter) {
        courseRepository.deleteById(parameter.getId());
        return true;
    }

    @Override
    public CourseDto getById(long id) {
        return courseRepository.findById(id).map(CourseDto::of).orElse(null);
    }

    @Override
    public boolean set(CourseInput parameter) {
        Optional<Course> courseOptional = courseRepository.findById(parameter.getId());
        if(!courseOptional.isPresent()){
            // error
            return false;
        }
        Course course = courseOptional.get();
        course.setSubject(parameter.getSubject());
        course.setUdtDt(LocalDateTime.now());
        course.setCategoryId(parameter.getCategoryId());
        courseRepository.save(course);

        return true;
    }
}
