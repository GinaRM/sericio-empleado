package com.gina.servicio_empleado.exception;

public class EmpleadoDuplicadoException extends RuntimeException{
    public EmpleadoDuplicadoException(String numeroDocumento) {
        super("Ya existe un empleado con el número de documento: " + numeroDocumento);
    }
}
