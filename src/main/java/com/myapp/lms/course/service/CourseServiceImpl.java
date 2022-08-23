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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private LocalDate getLocalDate(String value){
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
            return LocalDate.parse(value, formatter);
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public boolean add(CourseInput parameter) {
        LocalDate saleEndDt = getLocalDate(parameter.getSaleEndDtText());

        Course course = Course.builder()
                        .categoryId(parameter.getCategoryId())
                        .subject(parameter.getSubject())
                        .keyword(parameter.getKeyword())
                        .summary(parameter.getSummary())
                        .contents(parameter.getContents())
                        .price(parameter.getPrice())
                        .salePrice(parameter.getSalePrice())
                        .saleEndDt(saleEndDt)
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
        LocalDate saleEndDt = getLocalDate(parameter.getSaleEndDtText());

        Optional<Course> courseOptional = courseRepository.findById(parameter.getId());
        if(!courseOptional.isPresent()){
            // error
            return false;
        }
        Course course = courseOptional.get();
        course.setCategoryId(parameter.getId());
        course.setSubject(parameter.getSubject());
        course.setKeyword(parameter.getKeyword());
        course.setSummary(parameter.getSummary());
        course.setContents(parameter.getContents());
        course.setPrice(parameter.getPrice());
        course.setSalePrice(parameter.getSalePrice());
        course.setSaleEndDt(saleEndDt);
        course.setUdtDt(LocalDateTime.now());

        courseRepository.save(course);
        return true;
    }
}
