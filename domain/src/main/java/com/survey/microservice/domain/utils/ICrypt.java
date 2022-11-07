package com.survey.microservice.domain.utils;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;

public interface ICrypt {
    String encode(String plainText);
    String decode(String encodedText) throws UnsupportedEncodingException;
    void setKey(SecretKey externalKey);
    SecretKey getDecodedKey(String encodedSecretKey);
    String getEncodedKey();
}
