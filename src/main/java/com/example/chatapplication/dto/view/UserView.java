package com.example.chatapplication.dto.view;

import com.example.chatapplication.common.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserView {
    private Long id;
    private String username;
    private String email;
    private String phonnumber;
    private String password;
    private Date dob;
    private String address;
    private String fullname;
    private Integer gender;
    private Category.Role role;
}
