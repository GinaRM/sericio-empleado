package com.gina.servicio_empleado.repository;

import com.gina.servicio_empleado.model.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {
    Optional<EmpleadoEntity> findByNumeroDocumento(String numeroDocumento);

}
