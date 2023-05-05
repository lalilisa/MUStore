package com.example.chatapplication.controller;


import com.example.chatapplication.common.Utils;
import com.google.zxing.WriterException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("api")
public class TestController {

    @GetMapping(value = "get-qr",produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> test() throws WriterException, IOException {


        return ResponseEntity.ok(Utils.genQrCode("TRIMAIAAMAAMAMASdassdasdasdas"));
    }
}
