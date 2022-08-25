package com.myapp.lms.course.controller;

import com.myapp.lms.course.dto.TakeCourseDto;
import com.myapp.lms.course.model.ServiceResult;
import com.myapp.lms.course.model.TakeCourseParam;
import com.myapp.lms.course.service.TakeCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminTakeCourseController extends BaseController{
    private final TakeCourseService takeCourseService;

    @GetMapping("/admin/takeCourse/list.do")
    public String list(Model model, TakeCourseParam parameter){
        parameter.init();
        List<TakeCourseDto> courseList = takeCourseService.list(parameter);
        long totalCount = 0;
        if(!CollectionUtils.isEmpty(courseList)){
            totalCount = courseList.get(0).getTotalCount();
        }
        String queryString = parameter.getQueryString();
        String pagerHtml =
                getPaperHtml(totalCount, parameter.getPageSize(),
                        parameter.getPageIndex(), queryString);

        model.addAttribute("list", courseList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/takeCourse/list";
    }

    @PostMapping("/admin/takeCourse/status.do")
    public String status(
            TakeCourseParam takeCourseParam,
            Model model){
        ServiceResult result =
                takeCourseService.updateStatus(
                        takeCourseParam.getId(),
                        takeCourseParam.getStatus()
                );
        if(!result.isResult()){
            model.addAttribute("message", result.getMessage());
            return "common/error";
        }

        return "redirect:/admin/takeCourse/list.do";
    }
}
