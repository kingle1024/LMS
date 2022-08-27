package com.myapp.lms.course.service;

import com.myapp.lms.course.dto.TakeCourseDto;
import com.myapp.lms.course.entity.TakeCourse;
import com.myapp.lms.course.entity.TakeCourseCode;
import com.myapp.lms.course.mapper.TakeCourseMapper;
import com.myapp.lms.course.model.ServiceResult;
import com.myapp.lms.course.model.TakeCourseParam;
import com.myapp.lms.course.repository.TakeCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TakeCourseServiceImpl implements TakeCourseService{
    private final TakeCourseMapper takeCourseMapper;
    private final TakeCourseRepository takeCourseRepository;

    @Override
    public List<TakeCourseDto> list(TakeCourseParam parameter) {
        long totalCount = takeCourseMapper.selectListCount(parameter);
        List<TakeCourseDto> list = takeCourseMapper.selectList(parameter);
        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for(TakeCourseDto x : list){
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public boolean status(TakeCourseParam parameter) {
        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(parameter.getId());
        if(!optionalTakeCourse.isPresent()){
            return false;
        }

        TakeCourse takeCourse = optionalTakeCourse.get();
        takeCourse.setStatus(parameter.getStatus());
        takeCourseRepository.save(takeCourse);

        return true;
    }

    @Override
    public ServiceResult updateStatus(long id, String status) {
        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(id);
        if(!optionalTakeCourse.isPresent()){
            return new ServiceResult(false, "수강 정보가 없습니다.");
        }

        TakeCourse takeCourse = optionalTakeCourse.get();
        takeCourse.setStatus(status);
        takeCourseRepository.save(takeCourse);

        return new ServiceResult(true);
    }

    @Override
    public TakeCourseDto detail(long id) {
        Optional<TakeCourse> optionalTakeCourse =
                takeCourseRepository.findById(id);
        if(!optionalTakeCourse.isPresent()){
            return null;
        }
        return TakeCourseDto.of(optionalTakeCourse.get());
    }

    @Override
    public ServiceResult cancel(long id) {
        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(id);
        if(!optionalTakeCourse.isPresent()){
            return new ServiceResult(false, "수강 정보가 없습니다.");
        }
        TakeCourse takeCourse = optionalTakeCourse.get();
        takeCourse.setStatus(TakeCourseCode.STATUS_CANCEL);
        takeCourseRepository.save(takeCourse);

        return new ServiceResult(true);
    }

    @Override
    public List<TakeCourseDto> myCourse(String userId) {
        TakeCourseParam parameter = new TakeCourseParam();
        parameter.setUserId(userId);
        List<TakeCourseDto> list = takeCourseMapper.selectListMyCourse(parameter);

        return list;
    }
}
