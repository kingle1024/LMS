package com.myapp.lms.member.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class MemberInput {
//    @Id
    private String userId;
    private String userName;
    private String password;
    private String phone;

    private String newPassword;
}
