package com.example.chatapplication.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateUser {

    private String address;
    private String name;
    private Date dob;
    private String email;
    private String phonenumber;
}
