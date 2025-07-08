package com.gina.servicio_empleado.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class EmpleadoDto {
    @NotBlank private String nombres;
    @NotBlank private String apellidos;
    @NotBlank private String tipoDocumento;
    @NotBlank private String numeroDocumento;
    @NotNull private LocalDate fechaNacimiento;
    @NotNull private LocalDate fechaVinculacion;
    @NotBlank private String cargo;
    @NotNull @DecimalMin("0.0") private Double salario;


    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public LocalDate getFechaVinculacion() {
        return fechaVinculacion;
    }

    public String getCargo() {
        return cargo;
    }

    public Double getSalario() {
        return salario;
    }


}
