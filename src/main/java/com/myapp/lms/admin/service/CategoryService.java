package com.myapp.lms.admin.service;

import com.myapp.lms.admin.dto.CategoryDto;
import com.myapp.lms.admin.model.CategoryInput;
import java.util.List;

public interface CategoryService {
    List<CategoryDto> list();
    /**
     * 카테고리 신규 추가
     * @param categoryName
     * @return
     */
    boolean add(String categoryName);

    /**
     * 카테고리 수정
     * @param parameter
     * @return
     */
    boolean update(CategoryInput parameter);

    /**
     * 카테고리 삭제
     * @param id
     * @return
     */
    boolean del(long id);
    List<CategoryDto> frontList(CategoryDto parameter);
}
