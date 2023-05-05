package com.example.chatapplication.service.read;


import com.example.chatapplication.common.Category;
import com.example.chatapplication.common.Constant;
import com.example.chatapplication.common.Utils;
import com.example.chatapplication.domain.User;
import com.example.chatapplication.dto.view.UserView;
import com.example.chatapplication.exception.GeneralException;
import com.example.chatapplication.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;


    public UserView createUser(String username,String password,String email){
        User existedUser=userRepository.findByEmailOrUsername(email,username);
        if(existedUser!=null)
            throw new GeneralException(Constant.BAD_REQUEST,Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"Username and email is not exist");
        String hashPassword= Utils.hashPassword(password);
        User user=User.builder()
                .username(username)
                .password(hashPassword)
                .email(email)
                .gender(0)
                .role(Category.Role.USER)
                .build();

        return this.convertToView(userRepository.save(user));
    }

    public UserView login(String username,String password){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        User user=userRepository.findByUsername(username);
        if(user==null)
            throw new GeneralException(Constant.BAD_REQUEST,Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"User is not exist");
        if(!encoder.matches(password,user.getPassword()))
            throw new GeneralException(Constant.BAD_REQUEST,Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"Password is not correct");
        return this.convertToView(user);
    }


    private UserView convertToView(User domain){
        return UserView
                .builder()
                .id(domain.getId())
                .dob(domain.getDob())
                .address(domain.getAddress())
                .email(domain.getEmail())
                .fullname(domain.getFullname())
                .username(domain.getUsername())
                .phonnumber(domain.getPhonnumber())
                .role(domain.getRole())
                .gender(domain.getGender())
                .build();
    }
}
