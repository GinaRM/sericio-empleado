package com.gina.servicio_empleado.controller;

import com.gina.servicio_empleado.dto.EmpleadoDto;
import com.gina.servicio_empleado.service.EmpleadoProcessorService;
import com.gina.servicio_empleado.service.EmpleadoSoapClient;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/empleado")
public class EmpleadoController {
    private final EmpleadoProcessorService empleadoProcessorService;


    public EmpleadoController(EmpleadoProcessorService empleadoProcessorService) {
        this.empleadoProcessorService = empleadoProcessorService;

    }

    @PostMapping
    public ResponseEntity<?> procesarEmpleado(@Valid @RequestBody EmpleadoDto empleadoDto) {
        try {
            Map<String, Object> response = empleadoProcessorService.procesarEmpleado(empleadoDto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
