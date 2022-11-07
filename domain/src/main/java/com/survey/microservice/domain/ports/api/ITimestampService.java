package com.survey.microservice.domain.ports.api;

import java.sql.Timestamp;

public interface ITimestampService {
    String encryptTimeStamp(Timestamp timeStamp);
    Timestamp decryptTimeStamp(String timeStamp);
}
