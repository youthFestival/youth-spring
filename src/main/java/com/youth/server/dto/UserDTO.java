package com.youth.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * userId	String	아이디
 * password	String	비밀번호
 * email	String	이메일
 * gender	String	성별
 * username	String	성함
 * address	String	거주지
 * tel	String	전화번호
 * isAllowEmail	Boolean	메일 수신 동의 여부
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String userId;
    private String password;
    private String email;
    private String gender;
    private String username;
    private String address;
    private String tel;
    private Boolean isAllowEmail;
}