package com.myapp.lms.member.service;

import com.myapp.lms.admin.dto.MemberDto;
import com.myapp.lms.admin.model.MemberParam;
import com.myapp.lms.course.model.ServiceResult;
import com.myapp.lms.member.entity.Member;
import com.myapp.lms.member.model.MemberInput;
import com.myapp.lms.member.model.ResetPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.security.Provider;
import java.util.List;

public interface MemberService extends UserDetailsService {
    boolean register(MemberInput parameter);

    /**
     * uuid에 해당하는 계정을 활성화 함.
     * @param uuid
     * @return
     */
    boolean emailAuth(String uuid);

    /**
     * 입력한 이메일로 비밀번호 초기화 정보를 전송
     * @param resetPasswordInput
     * @return
     */
    boolean sendResetPassword(ResetPasswordInput resetPasswordInput);

    /**
     * 입력받은 UUID에 대해서 password로 비밀번호 초기화 진행
     * @param userId
     * @param password
     * @return
     */
    boolean resetPassword(String userId, String password);

    /**
     * 입력 받은 UUID 값이 유효한지 확인
     * @param uuid
     * @return
     */
    boolean checkResetPassword(String uuid);

    /**
     * 회원 목록 리턴(관리자에서만 사용 가능)
     * @return
     */
    List<MemberDto> list(MemberParam memberParam);

    /**
     * 회원 상세 정보
     * @param userId
     * @return
     */
    MemberDto detail(String userId);

    /**
     * 회원 상태 변경
     * @param userId
     * @param userStatus
     * @return
     */
    boolean updateStatus(String userId, String userStatus);

    /**
     * 회원 비밀번호 초기화
     * @param userId
     * @param password
     * @return
     */
    boolean updatePassword(String userId, String password);
    /**
     *  회원 정보 페이지에서 비밀번호 변경 기능
     */
    ServiceResult updateMemberPassword(MemberInput parameter);

    /**
     * 회원 정보 수정
     * @param parameter
     * @return
     */
    ServiceResult updateMember(MemberInput parameter);

    ServiceResult withdraw(String userId, String password);

    boolean loginHistory(HttpServletRequest request);
}
