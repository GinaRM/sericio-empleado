package com.gina.servicio_empleado.controller;

import com.gina.servicio_empleado.dto.EmpleadoDto;
import com.gina.servicio_empleado.dto.EmpleadoResponseDto;
import com.gina.servicio_empleado.service.EmpleadoProcessorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/api/empleado")
public class EmpleadoController {
    private final EmpleadoProcessorService empleadoProcessorService;


    public EmpleadoController(EmpleadoProcessorService empleadoProcessorService) {
        this.empleadoProcessorService = empleadoProcessorService;

    }

    @PostMapping
    public ResponseEntity<EmpleadoResponseDto> procesarEmpleado(@Valid @RequestBody EmpleadoDto empleadoDto) {
        EmpleadoResponseDto response = empleadoProcessorService.procesarEmpleado(empleadoDto);
        return ResponseEntity.ok(response);
    }
}
