package com.gina.servicio_empleado.service;

import com.gina.servicio_empleado.dto.EmpleadoDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmpleadoProcessorService {
    private final EmpleadoSoapClient empleadoSoapClient;

    public EmpleadoProcessorService(EmpleadoSoapClient empleadoSoapClient) {
        this.empleadoSoapClient = empleadoSoapClient;
    }

    public Map<String, Object> procesarEmpleado(EmpleadoDto empleadoDto) {
        validarEmpleado(empleadoDto);

        Period edad = Period.between(empleadoDto.getFechaNacimiento(), LocalDate.now());
        Period tiempoVinculacion = Period.between(empleadoDto.getFechaVinculacion(), LocalDate.now());

        empleadoSoapClient.enviarEmpleado(empleadoDto);

        Map<String, Object> response = new HashMap<>();
        response.put("empleado", empleadoDto);
        response.put("edad", formatPeriod(edad));
        response.put("tiempoVinculacion", formatPeriod(tiempoVinculacion));

        return response;
    }

    private void validarEmpleado(EmpleadoDto empleadoDto) {
        if (empleadoDto.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha de nacimiento inválida");
        }

        if (empleadoDto.getFechaVinculacion().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha de vinculación inválida");
        }

        Period edad = Period.between(empleadoDto.getFechaNacimiento(), LocalDate.now());
        if (edad.getYears() < 18) {
            throw new IllegalArgumentException("El empleado debe ser mayor de edad");
        }
    }

    private String formatPeriod(Period period) {
        return String.format("%d años, %d meses, %d días",
                period.getYears(), period.getMonths(), period.getDays());
    }
}
