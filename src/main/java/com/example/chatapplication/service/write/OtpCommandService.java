package com.example.chatapplication.service.write;

import com.example.chatapplication.common.Category;
import com.example.chatapplication.domain.User;
import com.example.chatapplication.domain.UserOtp;
import com.example.chatapplication.dto.view.SendOtpView;
import com.example.chatapplication.exception.GeneralException;
import com.example.chatapplication.repo.OtpRepositpry;
import com.example.chatapplication.repo.UserOtpRepository;
import com.example.chatapplication.repo.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpCommandService {

    @Value("${TWILIO_ACCOUNT_SID}")
    private String sid;
    @Value("${TWILIO_AUTH_TOKEN}")
    private String authToken;
    @Value("${HOST_PHONE}")
    private String hostPhone;
    private final UserRepository userRepository;
    private final OtpRepositpry otpRepositpry;

    private final UserOtpRepository userOtpRepository;
    public SendOtpView sendOtp(String phonenumber){
        String otp="300801";
        String formatPhone=phonenumber.replaceFirst("0","+84");
        Message message=Message.creator(
                        new com.twilio.type.PhoneNumber(formatPhone),
                        new com.twilio.type.PhoneNumber(hostPhone),otp
                        )
                .create();
        if(message.getStatus().equals(Message.Status.FAILED))
            throw new GeneralException(Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"send otp fail");
        User user=userRepository.findByPhonenumber(phonenumber);
        UUID uuid = UUID.randomUUID();
        if(user==null)
            throw new GeneralException(Category.ErrorCodeEnum.INVALID_FORMAT.name(),"User is not exist");
        userOtpRepository.save(UserOtp.builder().otp(otp).transactionId(uuid.toString()).userId(user.getId()).isExpire(0).build());
        return SendOtpView.builder().transactionId(uuid.toString()).otp(otp).build();
    }

}
