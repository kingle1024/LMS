package com.myapp.lms.admin.controller;

import com.myapp.lms.admin.dto.MemberDto;
import com.myapp.lms.admin.model.MemberParam;
import com.myapp.lms.admin.model.MemberInput;
import com.myapp.lms.course.controller.BaseController;
import com.myapp.lms.member.service.MemberService;
import com.myapp.lms.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminMemberController extends BaseController {
    private final MemberService memberService;

//    public AdminMemberController(MemberService memberService){
//        this.memberService = memberService;
//    }

    @GetMapping("/admin/member/list.do")
    public String list(Model model, MemberParam parameter){
        parameter.init(); // 먼저 초기화 진행
        List<MemberDto> members = memberService.list(parameter); // 검색

        long totalCount = 0;
        if(members != null && members.size() > 0){
            totalCount = members.get(0).getTotalCount(); // 결과가 몇개가 있냐
        }
        
        String queryString = parameter.getQueryString(); // 검색 조건이 2개 이상일 수가 있 queryString
        String pagerHtml =
                getPaperHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(), queryString);

        model.addAttribute("list", members);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/member/list";
    }

    @GetMapping("/admin/member/detail.do")
    public String detail(Model model, MemberParam parameter){
        parameter.init();
        MemberDto member = memberService.detail(parameter.getUserId());
        model.addAttribute("member", member);

        return "admin/member/detail";
    }

    @PostMapping("/admin/member/status.do")
    public String status(MemberInput parameter){
        boolean result = memberService.updateStatus(parameter.getUserId(), parameter.getUserStatus());
        return "redirect:/admin/member/detail.do?userId="+ parameter.getUserId();
    }

    @PostMapping("/admin/member/password.do")
    public String password(MemberInput parameter){
        boolean result = memberService.updatePassword(parameter.getUserId(), parameter.getPassword());
        return "redirect:/admin/member/detail.do?userId=" + parameter.getUserId();
    }
}
