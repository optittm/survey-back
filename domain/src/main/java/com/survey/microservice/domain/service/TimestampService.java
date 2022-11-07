package com.survey.microservice.domain.service;

import com.survey.microservice.domain.ports.api.ITimestampService;
import com.survey.microservice.domain.ports.spi.IOttmRepository;
import com.survey.microservice.domain.utils.AESCrypt;
import com.survey.microservice.domain.utils.ICrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Optional;

public class TimestampService implements ITimestampService {
    private static final Logger logger = LoggerFactory.getLogger(TimestampService.class);

    private final ICrypt cryptoUtil;

    public TimestampService(ICrypt crypt, IOttmRepository apiService){
        this.cryptoUtil = crypt;
        Optional<String> externalSecretKey = apiService.getSecretKey();
        externalSecretKey.ifPresentOrElse(
                keyString -> {
                    SecretKey key = cryptoUtil.getDecodedKey(keyString);
                    this.cryptoUtil.setKey(key);
                },
                () -> {
                    String keyEncoded = cryptoUtil.getEncodedKey();
                    apiService.saveEncodedKey(keyEncoded);
                });
    }

    public String encryptTimeStamp(Timestamp timeStamp){
        logger.info("Timestamp is starting encrypting");
        return cryptoUtil.encode(timeStamp.toString());
    }

    public Timestamp decryptTimeStamp(String timeStamp) {
        logger.info("Timestamp is starting decrypting");
        try {
            return Timestamp.valueOf(cryptoUtil.decode(timeStamp));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
