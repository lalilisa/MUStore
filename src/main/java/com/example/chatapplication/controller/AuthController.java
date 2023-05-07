package com.example.chatapplication.controller;


import com.example.chatapplication.common.Constant;
import com.example.chatapplication.common.Utils;
import com.example.chatapplication.dto.request.LoginRequest;
import com.example.chatapplication.dto.request.OtpVerifi;
import com.example.chatapplication.dto.request.PhonenumberRequest;
import com.example.chatapplication.dto.request.RegisterRequest;
import com.example.chatapplication.dto.response.LoginResponse;
import com.example.chatapplication.dto.response.OtpResponse;
import com.example.chatapplication.dto.view.UserView;
import com.example.chatapplication.service.read.UserQueryService;
import com.example.chatapplication.service.write.OtpCommandService;
import com.example.chatapplication.util.JwtTokenUtil;
import com.google.zxing.WriterException;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    @Value("${HOST_PHONE}")
    private String hostPhone;

    private final UserQueryService userQueryService;

    private final JwtTokenUtil jwtTokenUtil;

    private final OtpCommandService otpCommandService;
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        UserView user=userQueryService.login(loginRequest.getUsername(),loginRequest.getPassword());
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

    @PostMapping("send-otp")
    public ResponseEntity<?> sendotp(@RequestBody PhonenumberRequest request){
        String phone=request.getPhonenumber();
        return ResponseEntity.ok(otpCommandService.sendOtp(phone));
    }


    @PostMapping("verifi-otp")
    public ResponseEntity<?> verifi(@RequestBody OtpVerifi request){
        return ResponseEntity.ok(otpCommandService.verifiOtp(request.getTransactionId(),request.getOtp()));
    }
    private final static String symbol="%";
    @GetMapping(value = "get-qr",produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> genQrLogin(Authentication authentication, HttpServletResponse response) throws IOException, WriterException {
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();
        long expireTime=new Date().getTime()+(long)60000;
        String socketKey= UUID.randomUUID().toString();
        Cookie cookie = new Cookie("socketKey",socketKey);
        cookie.setMaxAge(600);
        response.addCookie(cookie);
        String raw=userDetails.getUsername().concat(symbol).concat(Long.toString(expireTime)).concat(symbol).concat(socketKey);
        return ResponseEntity.ok(Utils.genQrCode(raw));
    }
}
