package com.gina.servicio_empleado.controller;

import com.gina.servicio_empleado.dto.EmpleadoDto;
import com.gina.servicio_empleado.dto.EmpleadoResponseDto;
import com.gina.servicio_empleado.service.EmpleadoProcessorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
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

    @GetMapping("/paginado")
    public ResponseEntity<Page<EmpleadoResponseDto>> getAllPaginado(
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(empleadoProcessorService.obtenerEmpleadosPaginados(pageable));
    }

}
