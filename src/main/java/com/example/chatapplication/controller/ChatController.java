package com.example.chatapplication.controller;


import com.example.chatapplication.service.read.ChatMessageService;
import com.example.chatapplication.socket.datalistner.ChatData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    @GetMapping("room")
    public List<ChatData> getMessaege(@RequestParam("u1") Long u1,
                                      @RequestParam("u2") Long u2
                                         ){
        return chatMessageService.getMessageRoom(u1,u2);
    }
}
