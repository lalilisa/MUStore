package com.example.chatapplication.repo;

import com.example.chatapplication.domain.RoomChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomChatRepository extends JpaRepository<RoomChat,Long> {

    Optional<RoomChat> findRoomChatByAdminIdAndUserId(Long adminId,Long userId);
}
