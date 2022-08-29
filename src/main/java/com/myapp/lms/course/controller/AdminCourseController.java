package com.myapp.lms.course.controller;

import com.myapp.lms.admin.service.CategoryService;
import com.myapp.lms.course.dto.CourseDto;
import com.myapp.lms.course.model.CourseInput;
import com.myapp.lms.course.model.CourseParam;
import com.myapp.lms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@Slf4j
public class AdminCourseController extends BaseController{
    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping("/admin/course/list.do")
    public String list(Model model, CourseParam parameter){
        parameter.init();
        List<CourseDto> courseList = courseService.list(parameter);
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

        return "admin/course/list";
    }

    @GetMapping(value= {"/admin/course/add.do", "/admin/course/edit.do"})
    public String add(HttpServletRequest request,
            CourseInput parameter, Model model) {

        model.addAttribute("category", categoryService.list());
        boolean editMode = request.getRequestURI().contains("/edit.do");
        CourseDto detail = new CourseDto();

        if(editMode){
            long id = parameter.getId();
            CourseDto existCourse = courseService.getById(id);
            if(existCourse == null){
                // error
                model.addAttribute("message", "강좌 정보가 존재하지 않습니다.");
                return "common/error";
            }
            detail = existCourse;
        }

        model.addAttribute("editMode", editMode);
        model.addAttribute("detail", detail);
        return "admin/course/add";
    }
    private String[] getNewSaveFile(String baseLocalPath, String baseUrlPath, String originalFileName){
        LocalDate now = LocalDate.now();
        String[] dirs = {
                String.format("%s/%d/", baseLocalPath, now.getYear()),
                String.format("%s/%d/%02d/", baseLocalPath, now.getYear(), now.getMonthValue()),
                String.format("%s/%d/%02d/", baseLocalPath, now.getYear(), now.getMonthValue(), now.getDayOfMonth())
        };
        String urlDir = String.format("%s/%d/%02d/", baseLocalPath, now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        for(String dir : dirs){
            File file = new File(dir);
            if(!file.isDirectory()){
                file.mkdir();
            }
        }

        String fileExtension = "";
        if(originalFileName != null){
            int dotPos = originalFileName.lastIndexOf(".");
            if(dotPos > -1){
                fileExtension = originalFileName.substring(dotPos + 1);
            }
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String newFileName = String.format("%s%s", dirs[2], uuid);
        String newUrlFileName = String.format("%s%s", urlDir, uuid);
        if(fileExtension.length() > 0){
            newFileName += "." + fileExtension;
            newUrlFileName += "." + fileExtension;
        }
        String[] returnFileName = {newFileName, newUrlFileName};
        return returnFileName;
    }
    @PostMapping(value={"/admin/course/add.do", "/admin/course/edit.do"})
    public String addSubmit(
            Model model,
            CourseInput parameter,
            HttpServletRequest request,
            MultipartFile file){

        String saveFileName = "";
        String urlFileName = "";

        if(file != null){
            String originalFileName = file.getOriginalFilename();

            String baseLocalPath = "/home/kingle/IdeaProjects/lms/files";
            String baseUPath = "/files";

            String[] arrFileName = getNewSaveFile(originalFileName, baseLocalPath, originalFileName);

            saveFileName = arrFileName[0];
            urlFileName = arrFileName[1];

            try{
                File newFile = new File(saveFileName);
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(newFile));
            }catch (IOException e){
                log.info(e.getMessage());
            }
        }
        parameter.setFileName(saveFileName);
        parameter.setUrlFileName(urlFileName);
        boolean editMode = request.getRequestURI().contains("/edit.do");
        if(editMode){
            long id = parameter.getId();
            CourseDto existCourse = courseService.getById(id);
            if(existCourse == null){
                model.addAttribute("message", "강좌가 존재하지 않습니다.");
                return "common/error";
            }
            boolean result = courseService.set(parameter);
        }else{
            boolean result = courseService.add(parameter);
        }

        return "redirect:/admin/course/list.do";
    }
    @PostMapping("/admin/course/delete.do")
    public String del(CourseInput parameter){
        boolean result = courseService.del(parameter.getIdList());
        return "redirect:/admin/course/list.do";
    }
}
