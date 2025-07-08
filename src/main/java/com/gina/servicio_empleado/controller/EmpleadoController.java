package com.gina.servicio_empleado.controller;

import com.gina.servicio_empleado.dto.EmpleadoDto;
import com.gina.servicio_empleado.service.EmpleadoSoapClient;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/empleado")
public class EmpleadoController {
    private EmpleadoSoapClient empleadoSoapClient;

    public EmpleadoController(EmpleadoSoapClient empleadoSoapClient) {
        this.empleadoSoapClient = empleadoSoapClient;
    }

    @GetMapping
    public ResponseEntity<?> procesarEmpleado(@Valid EmpleadoDto empleadoDto) {
        // Validar fechas
        if (empleadoDto.getFechaNacimiento().isAfter(LocalDate.now())) {
            return ResponseEntity.badRequest().body("Fecha de nacimiento inválida");
        }

        if (empleadoDto.getFechaVinculacion().isAfter(LocalDate.now())) {
            return ResponseEntity.badRequest().body("Fecha de vinculación inválida");
        }

        // Validar mayor de edad
        Period edad = Period.between(empleadoDto.getFechaNacimiento(), LocalDate.now());
        if (edad.getYears() < 18) {
            return ResponseEntity.badRequest().body("El empleado debe ser mayor de edad");
        }

        // Calcular edad y tiempo de vinculación
        Period tiempoVinculacion = Period.between(empleadoDto.getFechaVinculacion(), LocalDate.now());

        // Invocar servicio SOAP
        empleadoSoapClient.enviarEmpleado(empleadoDto);

        // Respuesta con edad y vinculación
        Map<String, Object> response = new HashMap<>();
        response.put("empleado", empleadoDto);
        response.put("edad", formatPeriod(edad));
        response.put("tiempoVinculacion", formatPeriod(tiempoVinculacion));

        return ResponseEntity.ok(response);
    }

    private String formatPeriod(Period period) {
        return String.format("%d años, %d meses, %d días",
                period.getYears(), period.getMonths(), period.getDays());
    }
}
