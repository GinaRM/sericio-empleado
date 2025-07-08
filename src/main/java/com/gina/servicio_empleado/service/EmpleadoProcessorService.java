package com.gina.servicio_empleado.service;

import com.gina.servicio_empleado.dto.EmpleadoDto;
import com.gina.servicio_empleado.dto.EmpleadoResponseDto;
import com.gina.servicio_empleado.exception.EmpleadoDuplicadoException;
import com.gina.servicio_empleado.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmpleadoProcessorService {

    private final EmpleadoSoapClient empleadoSoapClient;
    private final EmpleadoRepository empleadoRepository;

    public EmpleadoProcessorService(EmpleadoSoapClient empleadoSoapClient,
                                    EmpleadoRepository empleadoRepository) {
        this.empleadoSoapClient = empleadoSoapClient;
        this.empleadoRepository = empleadoRepository;
    }

    public EmpleadoResponseDto procesarEmpleado(EmpleadoDto empleadoDto) {
        validarEmpleado(empleadoDto);
        validarDuplicado(empleadoDto);

        Period edad = calcularEdad(empleadoDto.getFechaNacimiento());
        Period tiempoVinculacion = calcularTiempoVinculacion(empleadoDto.getFechaVinculacion());

        empleadoSoapClient.enviarEmpleado(empleadoDto);

        return buildResponse(empleadoDto, edad, tiempoVinculacion);
    }

    private void validarEmpleado(EmpleadoDto dto) {
        if (dto.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha de nacimiento inválida");
        }

        if (dto.getFechaVinculacion().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha de vinculación inválida");
        }

        Period edad = calcularEdad(dto.getFechaNacimiento());
        if (edad.getYears() < 18) {
            throw new IllegalArgumentException("El empleado debe ser mayor de edad");
        }
    }

    private void validarDuplicado(EmpleadoDto dto) {
        boolean existe = empleadoRepository.findByNumeroDocumento(dto.getNumeroDocumento()).isPresent();
        if (existe) {
            throw new EmpleadoDuplicadoException(dto.getNumeroDocumento());
        }
    }

    private Period calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now());
    }

    private Period calcularTiempoVinculacion(LocalDate fechaVinculacion) {
        return Period.between(fechaVinculacion, LocalDate.now());
    }

    private EmpleadoResponseDto buildResponse(EmpleadoDto dto, Period edad, Period vinculo) {
        return new EmpleadoResponseDto(
                dto,
                formatPeriod(edad),
                formatPeriod(vinculo)
        );
    }

    private String formatPeriod(Period period) {
        return String.format("%d años, %d meses, %d días",
                period.getYears(), period.getMonths(), period.getDays());
    }
}