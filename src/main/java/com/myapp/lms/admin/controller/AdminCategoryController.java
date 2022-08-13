package com.myapp.lms.admin.controller;

import com.myapp.lms.admin.dto.CategoryDto;
import com.myapp.lms.admin.entity.Category;
import com.myapp.lms.admin.model.CategoryInput;
import com.myapp.lms.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminCategoryController {
    private final CategoryService categoryService;

    @GetMapping("/admin/category/list.do")
    public String list(Model model){
        List<CategoryDto> list = categoryService.list();
        model.addAttribute("list", list);
        return "admin/category/list";
    }
    @PostMapping("/admin/category/add.do")
    public String add(Model model, CategoryInput parameter){
        boolean result = categoryService.add(parameter.getCategoryName());
        System.out.println("result = " + result);
        return "redirect:/admin/category/list.do";
    }
    @PostMapping("/admin/category/delete.do")
    public String del(CategoryInput parameter){
        boolean result = categoryService.del(parameter.getId());
        return "redirect:/admin/category/list.do";
    }
    @PostMapping("/admin/category/update.do")
    public String update(CategoryInput parameter){
        categoryService.update(parameter);
        return "redirect:/admin/category/list.do";
    }
}
