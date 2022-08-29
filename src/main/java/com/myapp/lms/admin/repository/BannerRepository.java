package com.myapp.lms.admin.repository;

import com.myapp.lms.admin.entity.Banner;
import com.myapp.lms.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    long countBy();
}
