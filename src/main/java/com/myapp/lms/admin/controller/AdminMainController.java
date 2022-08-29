package com.myapp.lms.admin.controller;

import com.myapp.lms.admin.dto.BannerDto;
import com.myapp.lms.admin.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminMainController {
    private final BannerService bannerService;
    @GetMapping("/admin/main.do")
    public String main(Model model){
        List<BannerDto> banner = bannerService.frontList();
        model.addAttribute("list", banner);
        return "admin/main";
    }
}
