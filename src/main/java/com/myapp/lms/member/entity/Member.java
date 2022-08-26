package com.myapp.lms.member.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Member implements MemberCode{
    @Id
    private String userId;
    private String userName;
    private String phone;
    private String password;
    private LocalDateTime regDt;
    private LocalDateTime udtDt;
    private boolean emailAuthYn;
    private String emailAuthKey;
    private LocalDateTime emailAuthDt;

    private String resetPasswordKey;
    private LocalDateTime resetPasswordLimitDt;
    // 관리자여부를 지정할꺼냐?
    // 회원에 따른 ROLE을 지정할꺼냐?
    private boolean adminYn;
    private String userStatus; // 이용 가능한 상태, 정지 상태
}
