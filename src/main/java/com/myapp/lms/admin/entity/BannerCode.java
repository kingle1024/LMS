package com.myapp.lms.admin.entity;

import java.util.HashMap;

public class BannerCode {
    private String TARGET_BLANK = "_blank";
    private String TARGET_EMPTY = "";

    public HashMap<String, String> list(){
        HashMap<String, String> targetList = new HashMap<>();
        targetList.put(TARGET_BLANK, "새 창");
        targetList.put(TARGET_EMPTY, "현재 창");

        return targetList;
    }
}
