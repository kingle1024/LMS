package com.myapp.lms.member.model;

import com.myapp.lms.admin.model.CommonParam;
import com.myapp.lms.member.entity.LoginHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistoryDto extends CommonParam {
    //로그인 아이디와 로그인 날짜, 접속 IP, 접속 UserAgent
    private Long id;
    String userId;
    LocalDateTime logDt;
    String ip;
    String userAgent;

    private long totalCount;
    private long seq;

    public String getLogDtText(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return logDt != null ? logDt.format(formatter) : "";
    }

    public static List<LoginHistoryDto> of (List<LoginHistory> categories){
        if(categories != null){
            List<LoginHistoryDto> loginHistoryList = new ArrayList<>();
            for(LoginHistory x : categories){
                loginHistoryList.add(of(x));
            }
            return loginHistoryList;
        }
        return null;
    }

    public static LoginHistoryDto of (LoginHistory loginHistory){
        return LoginHistoryDto.builder()
                .id(loginHistory.getId())
                .userId(loginHistory.getUserId())
                .logDt(loginHistory.getLogDt())
                .ip(loginHistory.getIp())
                .userAgent(loginHistory.getUserAgent())
                .build();
    }
}
