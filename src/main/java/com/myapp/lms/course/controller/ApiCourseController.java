package com.myapp.lms.course.controller;

import com.myapp.lms.admin.service.CategoryService;
import com.myapp.lms.course.model.ServiceResult;
import com.myapp.lms.course.model.TakeCourseInput;
import com.myapp.lms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class ApiCourseController extends BaseController{
    private final CourseService courseService;
    private final CategoryService categoryService;

    @PostMapping("/api/course/req.api")
    public ResponseEntity<?> list(
            Model model,
            @RequestBody TakeCourseInput parameter,
            Principal principal
            ){
        parameter.setUserId(principal.getName());

        ServiceResult result = courseService.req(parameter);
        if(!result.isResult()){
            return ResponseEntity.ok().body(result.getMessage());
        }

        return ResponseEntity.ok().body(parameter);
    }
}
