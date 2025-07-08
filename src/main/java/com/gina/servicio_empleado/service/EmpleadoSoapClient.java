package com.gina.servicio_empleado.service;

import com.gina.servicio_empleado.dto.EmpleadoDto;

public interface EmpleadoSoapClient {
    void enviarEmpleado(EmpleadoDto empleadoDto);
}
