package com.myapp.lms.member.repository;

import com.myapp.lms.member.entity.LoginHistory;
import com.myapp.lms.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, String> {
    Optional<List<LoginHistory>> findAllByUserIdOrderByLogDtDesc(String userId);
}
