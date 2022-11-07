package com.survey.microservice.domain;

import com.survey.microservice.domain.utils.AESCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class AESCryptTests {

    private AESCrypt AESCrypt;

    @BeforeEach
    void init() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.AESCrypt = new AESCrypt();
    }

    @Test
    void testConversionSecretKey(){
        SecretKey secretKey = AESCrypt.getSecretKey();
        String encodedString = AESCrypt.getEncodedKey();
        SecretKey decodeKey = AESCrypt.getDecodedKey(encodedString);
        assertEquals(decodeKey, secretKey);
    }

    @Test
    void testEncodeDecodeTimestamp() throws UnsupportedEncodingException {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String tsCrypt = AESCrypt.encode(ts.toString());
        assertNotEquals(tsCrypt,toString());

        String tsDecrypted = AESCrypt.decode(tsCrypt);
        assertEquals(ts.toString(), tsDecrypted);
    }

}
