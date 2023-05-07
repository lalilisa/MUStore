package com.example.chatapplication.controller;


import com.example.chatapplication.common.Utils;
import com.example.chatapplication.socket.chat.ChatModule;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.ServerError;

@RestController
@RequestMapping("api")
@CrossOrigin
public class TestController {

    @Autowired
    private ChatModule chatModule;
    @GetMapping(value = "get-qr",produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> test() throws WriterException, IOException {


        return ResponseEntity.ok(Utils.genQrCode("TRIMAIAAMAAMAMASdassdasdasdas"));
    }

    @GetMapping("test")
    public ResponseEntity<?> testSocket() {
        chatModule.send();
        chatModule.onListeningVerifiQr();
        return ResponseEntity.ok("trimai");
    }
}
