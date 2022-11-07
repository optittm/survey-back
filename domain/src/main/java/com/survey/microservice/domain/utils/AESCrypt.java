package com.survey.microservice.domain.utils;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESCrypt implements ICrypt {

    private static final String ALGORITHM_NAME = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM_KEY_GENERATOR_NAME = "AES";
    private static final String ENCODED_CHAR_NAME = "UTF-8";
    private static final int NUMBER_BYTE_IV = 16;
    private static final int LENGTH_KEY = 128;

    private Cipher cipher;
    private final IvParameterSpec IV;
    private SecretKey secretKey;

    public AESCrypt() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance(ALGORITHM_NAME);
        this.IV = generateIv();
        this.secretKey = generateKey(LENGTH_KEY);
    }

    @Override
    public String encode(String input) {
        byte[] cipherText = new byte[0];

        try {
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey,this.IV);
            cipherText = cipher.doFinal(input.getBytes());
        } catch (InvalidKeyException
                | IllegalBlockSizeException
                | InvalidAlgorithmParameterException
                | BadPaddingException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    @Override
    public String decode(String cipherText) throws UnsupportedEncodingException {

        byte[] plainText = new byte[0];
        try {
            cipher.init(Cipher.DECRYPT_MODE, this.secretKey,this.IV);
            plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        } catch ( InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | BadPaddingException e) {
            e.printStackTrace();
        }

        return new String(plainText,ENCODED_CHAR_NAME);
    }

    @Override
    public void setKey(SecretKey externalKey) {
        this.secretKey = externalKey;
    }

    @Override
    public SecretKey getDecodedKey(String encodedSecretKey){
        byte[] decodedKey = Base64.getDecoder().decode(encodedSecretKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM_KEY_GENERATOR_NAME);
        return originalKey;
    }

    public String getEncodedKey(){
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    private SecretKey generateKey(int n){
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(ALGORITHM_KEY_GENERATOR_NAME);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[NUMBER_BYTE_IV];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public SecretKey getSecretKey(){
        return this.secretKey;
    }

}
