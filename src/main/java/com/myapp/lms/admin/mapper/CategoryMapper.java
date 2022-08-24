package com.myapp.lms.admin.mapper;

import com.myapp.lms.admin.dto.CategoryDto;
import com.myapp.lms.admin.dto.MemberDto;
import com.myapp.lms.admin.model.MemberParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<CategoryDto> select(CategoryDto parameter);
}
