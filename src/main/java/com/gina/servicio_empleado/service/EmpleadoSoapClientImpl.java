package com.gina.servicio_empleado.service;

import com.gina.servicio_empleado.dto.EmpleadoDto;
import com.gina.servicio_empleado.model.EmpleadoEntity;
import com.gina.servicio_empleado.repository.EmpleadoRepository;
import com.gina.servicio_empleado.util.EmpleadoMapper;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoSoapClientImpl implements EmpleadoSoapClient{
    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;

    public EmpleadoSoapClientImpl(EmpleadoRepository empleadoRepository, EmpleadoMapper empleadoMapper) {
        this.empleadoRepository = empleadoRepository;
        this.empleadoMapper = empleadoMapper;
    }


    @Override
    public void enviarEmpleado(EmpleadoDto empleadoDto) {
        EmpleadoEntity entity = empleadoMapper.toEntity(empleadoDto);
        empleadoRepository.save(entity);
    }
}
