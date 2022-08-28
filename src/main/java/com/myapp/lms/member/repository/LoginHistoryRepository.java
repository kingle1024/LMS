package com.myapp.lms.member.repository;

import com.myapp.lms.member.entity.LoginHistory;
import com.myapp.lms.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, String> {

}
