package com.tcc.manutencao_adm_ticket.exception;

public class ResourceAlreadyExistsException extends Exception{
    public ResourceAlreadyExistsException() {
    }

    public ResourceAlreadyExistsException(String msg) {
        super(msg);
    }
}
