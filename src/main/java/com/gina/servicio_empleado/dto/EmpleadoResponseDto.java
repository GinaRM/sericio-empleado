package com.gina.servicio_empleado.dto;

public class EmpleadoResponseDto {

    private EmpleadoDto empleado;
    private String edad;
    private String tiempoVinculacion;

    public EmpleadoResponseDto(EmpleadoDto empleado, String edad, String tiempoVinculacion) {
        this.empleado = empleado;
        this.edad = edad;
        this.tiempoVinculacion = tiempoVinculacion;
    }

    public EmpleadoDto getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoDto empleado) {
        this.empleado = empleado;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getTiempoVinculacion() {
        return tiempoVinculacion;
    }

    public void setTiempoVinculacion(String tiempoVinculacion) {
        this.tiempoVinculacion = tiempoVinculacion;
    }
}
