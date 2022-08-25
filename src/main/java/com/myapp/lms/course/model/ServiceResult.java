package com.myapp.lms.course.model;

import lombok.Data;

@Data
public class ServiceResult {
    private boolean result;
    private String message;

    public ServiceResult(){

    };

    public ServiceResult(boolean result, String message) {
        this.result = result;
        this.message = message;
    }
    public ServiceResult(boolean result){
        this.result = result;
    }
}
