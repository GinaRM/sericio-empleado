package com.gina.servicio_empleado.util;


import com.gina.servicio_empleado.dto.EmpleadoDto;
import com.gina.servicio_empleado.model.EmpleadoEntity;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoMapper {
    public EmpleadoEntity toEntity(EmpleadoDto dto) {
        EmpleadoEntity entity = new EmpleadoEntity();
        entity.setNombres(dto.getNombres());
        entity.setApellidos(dto.getApellidos());
        entity.setTipoDocumento(dto.getTipoDocumento());
        entity.setNumeroDocumento(dto.getNumeroDocumento());
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        entity.setFechaVinculacion(dto.getFechaVinculacion());
        entity.setCargo(dto.getCargo());
        entity.setSalario(dto.getSalario());
        return entity;


    }
}
