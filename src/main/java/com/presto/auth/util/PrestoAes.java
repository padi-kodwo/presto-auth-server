package com.presto.auth.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class PrestoAes {

    private static final Logger logger = LoggerFactory.getLogger(PrestoAes.class);
    private static String charsetName = "UTF-8";
    private static String algorithm = "AES/GCM/NoPadding";

    public static String encrypt(String data, String KEY) {
        if (KEY == null || data == null) {
            return null;
        }
        try {
            final byte[] keyByte =  Base64.getDecoder().decode(KEY.getBytes());
            final byte[] iv = new byte[16];
            SecretKey secretKey = new SecretKeySpec(keyByte, "AES");
            byte[] dataBytes = data.getBytes(charsetName);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
            String s = new String(Base64.getEncoder().encode(cipher.doFinal(dataBytes)));
            return s;
        } catch (Throwable e) {
            logger.debug("Exception In encryption: " + e.getMessage());
            logger.debug("Exception " + Arrays.toString(e.getStackTrace()).replaceAll(", ", "\n"));
            return null;
        }
    }

    public static String decrypt(String data, String KEY) {
        if (KEY == null || data == null) {
            return null;
        }
        try {
            byte[] dataBytes = Base64.getDecoder().decode(data.getBytes());
            final byte[] keyByte =  Base64.getDecoder().decode(KEY.getBytes());
            final byte[] iv = new byte[16];
            SecretKey secretKey = new SecretKeySpec(keyByte, "AES");
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
            byte[] byteDecryptedText = cipher.doFinal(dataBytes);
            return new String(byteDecryptedText);
        } catch (Exception e) {
            logger.debug("Exception In decryption: " + e.getMessage());
            logger.debug("Exception " + Arrays.toString(e.getStackTrace()).replaceAll(", ", "\n"));
            return null;
        }
    }
}
