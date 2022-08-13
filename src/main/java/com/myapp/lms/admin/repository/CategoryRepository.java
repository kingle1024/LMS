package com.myapp.lms.admin.repository;

import com.myapp.lms.admin.entity.Category;
import com.myapp.lms.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
//    Optional<Member> findByEmailAuthKey(String emailAuthKey);
//    Optional<Member> findByUserIdAndUserName(String userId, String userName);
//    Optional<Member> findByResetPasswordKey(String resetPasswordKey);
    Optional<Category> findByCategoryName(String categoryName);
//    Optional<List<Category>> findAllOrderBySortValueDesc();
}
