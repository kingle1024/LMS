package com.myapp.lms.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistory {
    //로그인 아이디와 로그인 날짜, 접속 IP, 접속 UserAgent
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String userId;
    LocalDateTime logDt;
    String ip;
    String userAgent;
}
