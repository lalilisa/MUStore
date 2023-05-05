package com.example.chatapplication.repo;

import com.example.chatapplication.domain.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OtpRepositpry extends JpaRepository<UserOtp,Long> {

}
