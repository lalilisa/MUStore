package com.example.chatapplication.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Utils {

    public static String hashPassword(String plainText){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        return encoder.encode(plainText);
    }

    public static byte[] genQrCode(String rawText) throws WriterException, IOException {
        String hasToken = DatatypeConverter.printBase64Binary(rawText.getBytes());
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(hasToken, BarcodeFormat.QR_CODE,350, 350);
        BufferedImage bufferedImage= MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        return baos.toByteArray();
    }
}
