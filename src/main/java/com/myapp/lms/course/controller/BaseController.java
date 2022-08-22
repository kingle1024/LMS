package com.myapp.lms.course.controller;

import com.myapp.lms.util.PageUtil;

public class BaseController {
    public String getPaperHtml(
            long totalCount, long pageSize, long pageIndex, String queryString){
        PageUtil pageUtil = new PageUtil(totalCount, pageSize, pageIndex, queryString);

        return pageUtil.paper();
    }
}
