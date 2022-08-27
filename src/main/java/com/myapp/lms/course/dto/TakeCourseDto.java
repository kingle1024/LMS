package com.myapp.lms.course.dto;

import com.myapp.lms.course.entity.Course;
import com.myapp.lms.course.entity.TakeCourse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TakeCourseDto {
    long id;
    long courseId;
    String userId;

    long payPrice; // 결제금액
    String status; // 상태(수강신청, 결제완료, 수강취소)

    LocalDateTime regDt; // 신청일

    // JOIN
    String userName;
    String phone;
    String subject;

    // add coloum
    long totalCount;
    long seq;

    public String getRegDtText(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return regDt != null ? regDt.format(formatter) : "";
    }
    public static TakeCourseDto of(TakeCourse x){
        return TakeCourseDto.builder()
                .id(x.getId())
                .userId(x.getUserId())
                .courseId(x.getCourseId())
                .payPrice(x.getPayPrice())
                .status(x.getStatus())
                .regDt(x.getRegDt())
                .build()
                ;
    }
}
