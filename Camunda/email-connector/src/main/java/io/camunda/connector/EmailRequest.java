package io.camunda.connector;

import io.camunda.connector.api.annotation.Secret;
import lombok.Data;

@Data
public class EmailRequest {

    @Secret
    private String from;
    @Secret
    private String password;
    @Secret
    private String host;

    private String to;

    private String subject;

    private String text;

}