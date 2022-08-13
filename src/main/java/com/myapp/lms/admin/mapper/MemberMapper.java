package com.myapp.lms.admin.mapper;

import com.myapp.lms.admin.dto.MemberDto;
import com.myapp.lms.admin.model.MemberParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    long selectListCount(MemberParam memberParam);
    List<MemberDto> selectList(MemberParam parameter);
}
