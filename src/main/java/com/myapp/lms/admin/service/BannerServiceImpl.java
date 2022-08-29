package com.myapp.lms.admin.service;

import com.myapp.lms.admin.dto.BannerDto;
import com.myapp.lms.admin.entity.Banner;
import com.myapp.lms.admin.model.BannerInput;
import com.myapp.lms.admin.model.BannerParam;
import com.myapp.lms.admin.repository.BannerRepository;
import com.myapp.lms.course.entity.Course;
import com.myapp.lms.course.entity.TakeCourse;
import com.myapp.lms.course.model.ServiceResult;
import com.myapp.lms.course.model.TakeCourseInput;
import com.myapp.lms.course.repository.CourseRepository;
import com.myapp.lms.course.repository.TakeCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BannerServiceImpl implements BannerService {
    private final CourseRepository courseRepository;
    private final TakeCourseRepository takeCourseRepository;
    private final BannerRepository bannerRepository;
    private LocalDate getLocalDate(String value){
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
            return LocalDate.parse(value, formatter);
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public boolean add(BannerInput parameter) {
        Banner banner = Banner.builder()
                        .bannerName(parameter.getBannerName())
                        .link(parameter.getLink())
                        .target(parameter.getTarget())
                        .rank(parameter.getRank())
                        .usingYn(parameter.isUsingYn())
                        .fileName(parameter.getFileName())
                        .urlFileName(parameter.getUrlFileName())
                        .regDt(LocalDateTime.now())
                        .build();

        bannerRepository.save(banner);

        return true;
    }

    @Override
    public List<BannerDto> list(BannerParam parameter) {
        long totalCount = bannerRepository.countBy();

        List<Banner> b = bannerRepository.findAll();
        List<BannerDto> list = BannerDto.of(b);
//        = courseMapper.selectList(parameter);
        if(!CollectionUtils.isEmpty(list)){
            int i = 0;
            for(BannerDto x : list){
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }
        return list;
    }

    @Override
    public boolean del(String idList) {
        if(idList !=  null && idList.length() > 0){
            String[] ids = idList.split(",");
            for(String x : ids){
                long id = 0L;
                try{
                    id = Long.parseLong(x);
                }catch(Exception e){}

                if (id > 0) {
                    bannerRepository.deleteById(id);
                }
            }
        }
        return true;
    }

    @Override
    public BannerDto getById(long id) {
        return bannerRepository.findById(id).map(BannerDto::of).orElse(null);
    }

    @Override
    public boolean set(BannerInput parameter) {
        Optional<Banner> bannerOptional = bannerRepository.findById(parameter.getId());
        if(!bannerOptional.isPresent()){
            // error
            return false;
        }

        Banner banner = bannerOptional.get();
        banner.setBannerName(parameter.getBannerName());
        banner.setFileName(parameter.getFileName());
        banner.setUrlFileName(parameter.getUrlFileName());
        banner.setLink(parameter.getLink());
        banner.setTarget(parameter.getTarget());
        banner.setRank(parameter.getRank());
        banner.setUsingYn(parameter.isUsingYn());
        banner.setRegDt(LocalDateTime.now());

        bannerRepository.save(banner);
        return true;
    }

    @Override
    public List<BannerDto> frontList() {
        // all
        Optional<List<Banner>> optionalBanners = bannerRepository.findByUsingYnTrueOrderByRankDesc();
        if(!optionalBanners.isPresent()){
            return null;
        }
        List<Banner> list = optionalBanners.get();

        return BannerDto.of(list);
    }

    @Override
    public BannerDto frontDetail(long courseId) {
        Optional<Banner> optionalCourse = bannerRepository.findById(courseId);
        if(!optionalCourse.isPresent()){
            return null;
        }
        return BannerDto.of(optionalCourse.get());
    }

    @Override
    public ServiceResult req(TakeCourseInput parameter) {
        ServiceResult result = new ServiceResult();
        Optional<Course> optionalCourse = courseRepository.findById(parameter.getCourseId());
        if(!optionalCourse.isPresent()){
            result.setResult(false);
            result.setMessage("강좌 정보가 존재하지 않습니다.");
            return result;
        }
        Course course = optionalCourse.get();
        String[] statusList = {TakeCourse.STATUS_REQ, TakeCourse.STATUS_COMPLETE};
        long count =
                takeCourseRepository.countByCourseIdAndUserIdAndStatusIn(
                        course.getId(), parameter.getUserId(),
                        Arrays.asList(statusList));

        if(count > 0){
            result.setResult(false);
            result.setMessage("이미 신청한 강좌 정보가 존재합니다.");
            return result;
        }

        TakeCourse takeCourse = TakeCourse.builder()
                .courseId(course.getId())
                .userId(parameter.getUserId())
                .payPrice(course.getSalePrice())
                .regDt(LocalDateTime.now())
                .status(TakeCourse.STATUS_REQ)
                .build();
        takeCourseRepository.save(takeCourse);

        result.setResult(true);
        result.setMessage("");

        return result;
    }

    @Override
    public List<BannerDto> listAll() {
        List<Banner> courseList = bannerRepository.findAll();
        return BannerDto.of(courseList);
    }
}
