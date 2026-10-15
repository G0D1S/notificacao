package com.igor.notificacao.infrastructure.exception;

import com.igor.notificacao.business.EmailService;

public class EmailException extends RuntimeException{

    public EmailException(String mensagem){
        super(mensagem);
    }

    public EmailException (String mensagem, Throwable throwable){
        super(mensagem);
    }
}
