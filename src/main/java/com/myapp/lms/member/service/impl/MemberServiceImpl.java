package com.myapp.lms.member.service.impl;

import com.myapp.lms.admin.dto.MemberDto;
import com.myapp.lms.admin.mapper.MemberMapper;
import com.myapp.lms.admin.model.MemberParam;
import com.myapp.lms.component.MailComponents;
import com.myapp.lms.course.model.ServiceResult;
import com.myapp.lms.member.entity.LoginHistory;
import com.myapp.lms.member.entity.Member;
import com.myapp.lms.member.entity.MemberCode;
import com.myapp.lms.member.exception.MemberNotEmailAuthException;
import com.myapp.lms.member.exception.MemberStopUserException;
import com.myapp.lms.member.model.LoginHistoryDto;
import com.myapp.lms.member.model.MemberInput;
import com.myapp.lms.member.model.ResetPasswordInput;
import com.myapp.lms.member.repository.LoginHistoryRepository;
import com.myapp.lms.member.repository.MemberRepository;
import com.myapp.lms.member.service.MemberService;
import com.myapp.lms.util.PasswordUtils;
import com.myapp.lms.util.RequestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;
    private final MemberMapper memberMapper;
    private final LoginHistoryRepository loginHistoryRepository;
    @Override
    public boolean register(MemberInput parameter) {
        Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
        if(optionalMember.isPresent()){
            // ?????? userId??? ???????????? ???????????? ????????? ??????
            return false;
        }
        String encPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());

        Member member = Member.builder()
                        .userId(parameter.getUserId())
                        .userName(parameter.getUserName())
                        .phone(parameter.getPhone())
                        .password(encPassword)
                        .regDt(LocalDateTime.now())
                        .emailAuthYn(false)
                        .emailAuthKey(UUID.randomUUID().toString())
                        .userStatus(Member.MEMBER_STATUS_REQ)
                        .build();

        memberRepository.save(member);

        sendMail(member);

        return true;
    }

    @Override
    public boolean emailAuth(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
        if(optionalMember.isEmpty()){
            return false;
        }
        Member member = optionalMember.get();
        if(member.isEmailAuthYn()){
            return false;
        }
        member.setUserStatus(MemberCode.MEMBER_STATUS_ING);
        member.setEmailAuthYn(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);
        return true;
    }

    @Override
    public boolean sendResetPassword(ResetPasswordInput resetPasswordInput) {
        Optional<Member> optionalMember =
                memberRepository.findByUserIdAndUserName(
                        resetPasswordInput.getUserId(),
                        resetPasswordInput.getUserName()
                );
        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("?????? ????????? ???????????? ????????????.");
        }
        Member member = optionalMember.get();
        String uuid = UUID.randomUUID().toString();
        member.setResetPasswordKey(uuid);
        member.setResetPasswordLimitDt(LocalDateTime.now().plusDays(1));
        memberRepository.save(member);

        String email = member.getUserId();
        String subject = "[lms] ???????????? ????????? ???????????????.";
        String text = "<p>" +
                "<a target='_blank' " +
                "href='http://localhost:8080/member/reset/password?id="+uuid+"'>???????????? ????????? ??????" +
                "</a>" +
                "</p>";
        mailComponents.sendMail(email, subject, text);
        return true;
    }

    @Override
    public boolean resetPassword(String uuid, String password) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
        System.out.println(uuid);
        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("?????? ????????? ???????????? ????????????.");
        }
        Member member = optionalMember.get();
        LocalDateTime resetPasswordLimitDt = member.getResetPasswordLimitDt();

        if(resetPasswordLimitDt == null){
            throw new RuntimeException("????????? ????????? ????????????.");
        }else if(resetPasswordLimitDt.isBefore(LocalDateTime.now())){
            throw new RuntimeException("????????? ????????? ????????????.");
        }

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        member.setPassword(encPassword);
        member.setResetPasswordKey("");
        member.setResetPasswordLimitDt(null);
        memberRepository.save(member);

        return true;
    }

    @Override
    public boolean checkResetPassword(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
        if(!optionalMember.isPresent()){
            return false;
        }
        Member member = optionalMember.get();
        if(member.getResetPasswordLimitDt() == null){
            throw new RuntimeException("????????? ????????? ????????????. ");
        }
        if(member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("????????? ????????? ????????????.");
        }

        return true;
    }

    @Override
    public List<MemberDto> list(MemberParam parameter) {
        long totalCount = memberMapper.selectListCount(parameter);
        List<MemberDto> list = memberMapper.selectList(parameter);

        if(!CollectionUtils.isEmpty(list)){ // empty??? ?????????
            int i = 0;
            for(MemberDto x : list){
                Optional<LoginHistory> optionalLoginHistory = loginHistoryRepository.findTop1ByUserIdOrderByLogDtDesc(x.getUserId());
                if(optionalLoginHistory.isPresent()){
                    LoginHistory loginHistory = optionalLoginHistory.get();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

                    if(loginHistory.getLogDt() != null){
                        String logText = loginHistory.getLogDt().format(formatter);
                        x.setLogDtText(logText);
                    }else{
                        x.setLogDtText("");
                    }
                }
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }
        return list;
    }

    @Override
    public MemberDto detail(String userId) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if(!optionalMember.isPresent()){
            return null;
        }
        Member member = optionalMember.get();
        return MemberDto.of(member);
    }

    @Override
    public boolean updateStatus(String userId, String userStatus) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("?????? ????????? ???????????? ????????????.");
        }
        Member member = optionalMember.get();
        member.setUserStatus(userStatus);
        memberRepository.save(member);
        return true;
    }

    @Override
    public boolean updatePassword(String userId, String password) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("?????? ????????? ???????????? ????????????.");
        }
        Member member = optionalMember.get();
        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        member.setPassword(encPassword);
        memberRepository.save(member);
        return true;
    }

    @Override
    public ServiceResult updateMemberPassword(MemberInput parameter) {
        Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
        if(!optionalMember.isPresent()){
            return new ServiceResult(false, "?????? ????????? ???????????? ????????????.");
        }
        Member member = optionalMember.get();
        if(!PasswordUtils.equals(parameter.getPassword(), member.getPassword())){
            return new ServiceResult(false, "??????????????? ???????????? ????????????.");
        }

        String encPassword = PasswordUtils.encPassword(parameter.getNewPassword());
        member.setPassword(encPassword);
        memberRepository.save(member);
        return new ServiceResult(true);
    }

    @Override
    public ServiceResult updateMember(MemberInput parameter) {
        Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());

        if(!optionalMember.isPresent()){
            return new ServiceResult(false, "?????? ????????? ???????????? ????????????.");
        }

        Member member = optionalMember.get();
        member.setPhone(parameter.getPhone());
        member.setZipcode(parameter.getZipcode());
        member.setAddr(parameter.getAddr());
        member.setAddrDetail(parameter.getAddrDetail());
        member.setUdtDt(LocalDateTime.now());
        memberRepository.save(member);

        return new ServiceResult(true);
    }

    @Override
    public ServiceResult withdraw(String userId, String password) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if(!optionalMember.isPresent()){
            return new ServiceResult(false, "?????? ????????? ???????????? ????????????.");
        }
        Member member = optionalMember.get();

        if(!PasswordUtils.equals(password, member.getPassword())){
            return new ServiceResult(false, "??????????????? ???????????? ????????????.");
        }

        member.setUserName("????????????");
        member.setPhone("");
        member.setPassword("");
        member.setRegDt(null);
        member.setUdtDt(null);
        member.setEmailAuthYn(false);
        member.setEmailAuthDt(null);
        member.setEmailAuthKey("");
        member.setResetPasswordKey("");
        member.setResetPasswordLimitDt(null);
        member.setUserStatus(MemberCode.MEMBER_STATUS_WITHDRAW);
        member.setZipcode("");
        member.setAddr("");
        member.setAddrDetail("");
        memberRepository.save(member);

        return new ServiceResult(true);
    }

    @Override
    public boolean loginHistory(HttpServletRequest request) {
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setUserId(request.getParameter("username"));
        loginHistory.setLogDt(LocalDateTime.now());
        loginHistory.setUserAgent(RequestUtils.getUserAgent(request));
        loginHistory.setIp(RequestUtils.getClientIP(request));
        loginHistoryRepository.save(loginHistory);

        return true;
    }

    @Override
    public List<LoginHistoryDto> loginHistoryById(String userId) {
        Optional<List<LoginHistory>> optionalLoginHistoryList =
                loginHistoryRepository.findAllByUserIdOrderByLogDtDesc(userId);

        if(!optionalLoginHistoryList.isPresent()){
            return null;
        }

        List<LoginHistory> loginHistoryList = optionalLoginHistoryList.get();
        List<LoginHistoryDto> list = LoginHistoryDto.of(loginHistoryList);

        return list;
    }

    private void sendMail(Member member) {
        String email = member.getUserId();
        String subject = "[lms] ?????? ???????????????.";
        String text = "<p><a target='_blank' href='http://localhost:8080/email-auth?id="+member.getEmailAuthKey()+"'>??????</a></p>";
        mailComponents.sendMail(email, subject, text);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findById(username);
        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("?????? ????????? ???????????? ????????????.");
        }
        Member member = optionalMember.get();

        if(Member.MEMBER_STATUS_REQ.equals(member.getUserStatus())){
            throw new MemberNotEmailAuthException("????????? ???????????? ????????? ??? ??? ????????????.");
        }
        if(Member.MEMBER_STATUS_STOP.equals(member.getUserStatus())){
            throw new MemberStopUserException("????????? ?????? ?????????.");
        }
        if(Member.MEMBER_STATUS_WITHDRAW.equals(member.getUserStatus())){
            throw new MemberStopUserException("????????? ?????? ?????????.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if(member.isAdminYn()){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
    }
}
