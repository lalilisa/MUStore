package com.example.chatapplication.controller;


import com.example.chatapplication.common.Constant;
import com.example.chatapplication.dto.request.LoginRequest;
import com.example.chatapplication.dto.request.RegisterRequest;
import com.example.chatapplication.dto.response.LoginResponse;
import com.example.chatapplication.dto.view.UserView;
import com.example.chatapplication.service.read.UserQueryService;
import com.example.chatapplication.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserQueryService userQueryService;

    private final JwtTokenUtil jwtTokenUtil;
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        UserView user=userQueryService.login(loginRequest.getUsername(),loginRequest.getPasswrod());
        Date expireAccess=new Date(System.currentTimeMillis() + Constant.JWT_TOKEN_VALIDITY * 1000);
        Date expireRefresh=new Date(System.currentTimeMillis() + Constant.JWT_TOKEN_VALIDITY * 10000);
        String accessToken=jwtTokenUtil.generateAccountToken(user,expireAccess);
        String refreshToken=jwtTokenUtil.generateAccountToken(user,expireRefresh);
        return ResponseEntity.ok(LoginResponse.builder()
                    .userId(user.getId())
                    .role(user.getRole())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expireAccressToken(expireAccess)
                    .build());
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        UserView user=userQueryService.createUser(registerRequest.getUsername(),registerRequest.getPassword(),registerRequest.getEmail());
        return ResponseEntity.ok(user);
    }
}
