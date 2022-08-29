package com.myapp.lms.admin.controller;

import com.myapp.lms.admin.dto.BannerDto;
import com.myapp.lms.admin.model.BannerInput;
import com.myapp.lms.admin.model.BannerParam;
import com.myapp.lms.admin.service.BannerService;
import com.myapp.lms.admin.service.CategoryService;
import com.myapp.lms.course.controller.BaseController;
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
public class AdminBannerController extends BaseController {
    private final CategoryService categoryService;
    private final BannerService bannerService;

    @GetMapping("/admin/banner/list.do")
    public String list(Model model, BannerParam parameter){
        parameter.init();
        List<BannerDto> bannerList = bannerService.list(parameter);
        long totalCount = 0;
        if(!CollectionUtils.isEmpty(bannerList)){
            totalCount = bannerList.get(0).getTotalCount();
        }
        String queryString = parameter.getQueryString();
        String pagerHtml =
                getPaperHtml(totalCount, parameter.getPageSize(),
                        parameter.getPageIndex(), queryString);
//        List<CategoryDto> list = categoryService.list();
//        model.addAttribute("list", list);

        model.addAttribute("list", bannerList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);
        return "admin/banner/list";
    }
    @GetMapping(value= {"/admin/banner/add.do", "/admin/banner/edit.do"})
    public String add(HttpServletRequest request, BannerDto bannerDto,
                      BannerInput parameter, Model model ) {
        System.out.println("front:"+parameter.isUsingYn());
        System.out.println("b_front:"+bannerDto.isUsingYn());
        boolean editMode = request.getRequestURI().contains("/edit.do");
        BannerDto detail = new BannerDto();

        if(editMode){
            long id = parameter.getId();
            BannerDto existCourse = bannerService.getById(id);
            if(existCourse == null){
                // error
                model.addAttribute("message", "배너 정보가 존재하지 않습니다.");
                return "common/error";
            }
            detail = existCourse;
        }

        model.addAttribute("editMode", editMode);
        model.addAttribute("banner", detail);
        return "admin/banner/add";
    }
    @PostMapping(value={"/admin/banner/add.do", "/admin/banner/edit.do"})
    public String add(
            Model model,
            BannerInput parameter,
            HttpServletRequest request,
            MultipartFile file){
//        boolean result = categoryService.add(parameter.getCategoryName());
        String saveFileName = "";
        String urlFileName = "";
        if(file != null){
            String originFileName = file.getOriginalFilename();

            String baseLocalPath = "/home/kingle/IdeaProjects/lms/files";
            String baseUPath = "/files";

            String[] arrFileName = getNewSaveFile(baseLocalPath, baseUPath, originFileName);

            saveFileName = arrFileName[0];
            urlFileName = arrFileName[1];

            try{
                File newFile = new File(saveFileName);
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(newFile));
            }catch(IOException e){
                log.info(e.getMessage());
            }
        }
        parameter.setFileName(saveFileName);
        parameter.setUrlFileName(urlFileName);
        parameter.setUsingYn(parameter.isUsingYn());
        boolean editMode = request.getRequestURI().contains("/edit.do");
        if(editMode){
            long id = parameter.getId();
            BannerDto existBanner = bannerService.getById(id);
            if(existBanner == null){
                model.addAttribute("message", "배너가 존재하지 않습니다.");
                return "common/error";
            }
            boolean result = bannerService.set(parameter);
        }else{
            boolean result = bannerService.add(parameter);
        }

        return "redirect:/admin/banner/list.do";
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
    @PostMapping("/admin/banner/delete.do")
    public String del(BannerInput parameter){
//        boolean result = categoryService.del(parameter.getId());
        return "redirect:/admin/banner/list.do";
    }
    @PostMapping("/admin/banner/update.do")
    public String update(BannerInput parameter){
//        categoryService.update(parameter);
        return "redirect:/admin/banner/list.do";
    }
}
