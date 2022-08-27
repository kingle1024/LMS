package com.myapp.lms.member.controller;

import com.myapp.lms.common.model.ResponseResult;
import com.myapp.lms.course.dto.TakeCourseDto;
import com.myapp.lms.course.model.ServiceResult;
import com.myapp.lms.course.model.TakeCourseInput;
import com.myapp.lms.course.service.TakeCourseService;
import com.myapp.lms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor // memberService의 생성자가 추가되지 않아도 됨
@RestController
public class ApiMemberController {

    private final MemberService memberService;
    private final TakeCourseService takeCourseService;

    @PostMapping("/api/member/course/cancel.api")
    public ResponseEntity<?> cancelCourse(
            @RequestBody TakeCourseInput parameter,
            Principal principal){
        // 내 강좌인지 확인
        String userId = principal.getName();
        TakeCourseDto detail =
                takeCourseService.detail(parameter.getTakeCourseId());
        if(detail == null)
            return ResponseEntity.ok().body(new ResponseResult(false, "수강 신청 정보가 존재하지 않습니다."));
        if(userId == null || !detail.getUserId().equals(userId)){
            return ResponseEntity.ok().body(
                    new ResponseResult(
                            false, "본인의 수강 신청 정보만 취소할 수 있습니다."
                    )
            );
        }

        ServiceResult result = takeCourseService.cancel(parameter.getTakeCourseId());
        if(!result.isResult()){
            ResponseResult responseResult =
                    new ResponseResult(false, result.getMessage());
            return ResponseEntity.ok().body(responseResult);
        }

        ResponseResult responseResult = new ResponseResult(true);
        return ResponseEntity.ok().body(responseResult);
    }
}
