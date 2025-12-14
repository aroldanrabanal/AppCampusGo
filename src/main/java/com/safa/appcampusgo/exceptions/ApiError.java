package com.safa.appcampusgo.exceptions;

import lombok.Data;

@Data
public class ApiError {
    private String mensaje;
    private int status;

    public ApiError(String mensaje, int status) {
        this.mensaje = mensaje;
        this.status = status;
    }
}