package com.gina.servicio_empleado.repository;

import com.gina.servicio_empleado.model.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {
}
