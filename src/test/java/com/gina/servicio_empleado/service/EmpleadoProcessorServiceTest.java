package com.gina.servicio_empleado.service;

import com.gina.servicio_empleado.dto.EmpleadoDto;
import com.gina.servicio_empleado.dto.EmpleadoResponseDto;
import com.gina.servicio_empleado.exception.EmpleadoDuplicadoException;
import com.gina.servicio_empleado.model.EmpleadoEntity;
import com.gina.servicio_empleado.repository.EmpleadoRepository;
import com.gina.servicio_empleado.util.EmpleadoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoProcessorServiceTest {

    @Mock
    EmpleadoSoapClient  empleadoSoapClient;

    @Mock
    EmpleadoRepository  empleadoRepository;

    @Mock
    EmpleadoMapper      empleadoMapper;

    @InjectMocks
    EmpleadoProcessorService service;

    private EmpleadoDto dtoBase;

    @BeforeEach
    void setup() {
        dtoBase = new EmpleadoDto();
        dtoBase.setNombres("Ana");
        dtoBase.setApellidos("Pérez");
        dtoBase.setTipoDocumento("CC");
        dtoBase.setNumeroDocumento("12345");
        dtoBase.setCargo("DEV");
        dtoBase.setSalario(4500.0);
    }

    private void setFechas(LocalDate nacimiento, LocalDate vinculo) {
        dtoBase.setFechaNacimiento(nacimiento);
        dtoBase.setFechaVinculacion(vinculo);
    }

    @Test
    void whenTodoValido_shouldReturnResponseAndCallSoap() {
        setFechas(LocalDate.now().minusYears(30), LocalDate.now().minusYears(2));
        when(empleadoRepository.findByNumeroDocumento("12345"))
                .thenReturn(Optional.empty());

        EmpleadoResponseDto resp = service.procesarEmpleado(dtoBase);

        assertThat(resp.getEdad()).startsWith("30 años");
        assertThat(resp.getTiempoVinculacion()).startsWith("2 años");
        verify(empleadoSoapClient, times(1)).enviarEmpleado(dtoBase);
    }

    @Test
    void whenNacimientoFutura_shouldThrowIllegalArgumentException() {
        setFechas(LocalDate.now().plusDays(1), LocalDate.now().minusYears(1));
        assertThatThrownBy(() -> service.procesarEmpleado(dtoBase))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fecha de nacimiento inválida");
    }

    @Test
    void whenVinculacionFutura_shouldThrowIllegalArgumentException() {
        setFechas(LocalDate.now().minusYears(30), LocalDate.now().plusDays(1));
        assertThatThrownBy(() -> service.procesarEmpleado(dtoBase))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fecha de vinculación inválida");
    }

    @Test
    void whenMenorDeEdad_shouldThrowIllegalArgumentException() {
        setFechas(LocalDate.now().minusYears(17), LocalDate.now().minusYears(1));
        assertThatThrownBy(() -> service.procesarEmpleado(dtoBase))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El empleado debe ser mayor de edad");
    }

    @Test
    void whenEdadMayorCien_shouldThrowIllegalArgumentException() {
        setFechas(LocalDate.now().minusYears(101), LocalDate.now().minusYears(50));
        assertThatThrownBy(() -> service.procesarEmpleado(dtoBase))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La edad del empleado no puede superar los 100 años");
    }

    @Test
    void whenDuplicado_shouldThrowEmpleadoDuplicadoException() {
        setFechas(LocalDate.now().minusYears(30), LocalDate.now().minusYears(2));
        when(empleadoRepository.findByNumeroDocumento("12345"))
                .thenReturn(Optional.of(new EmpleadoEntity()));

        assertThatThrownBy(() -> service.procesarEmpleado(dtoBase))
                .isInstanceOf(EmpleadoDuplicadoException.class)
                .hasMessageContaining("12345");
    }

    @Test
    void obtenerEmpleadosPaginados_shouldMapearCorrectamente() {
        // Preparamos tu entidad real
        EmpleadoEntity entidad = new EmpleadoEntity();
        entidad.setNombres("Luis");
        entidad.setApellidos("Gómez");
        entidad.setFechaNacimiento(LocalDate.now().minusYears(40));
        entidad.setFechaVinculacion(LocalDate.now().minusYears(5));
        entidad.setCargo("QA");
        entidad.setSalario(3200.0);

        Page<EmpleadoEntity> pageEnt = new PageImpl<>(
                List.of(entidad),
                PageRequest.of(0, 1),
                1
        );
        when(empleadoRepository.findAll(any(Pageable.class))).thenReturn(pageEnt);

        // Stub del mapper
        EmpleadoDto mapped = new EmpleadoDto();
        mapped.setNombres("Luis");
        mapped.setApellidos("Gómez");
        mapped.setFechaNacimiento(entidad.getFechaNacimiento());
        mapped.setFechaVinculacion(entidad.getFechaVinculacion());
        mapped.setCargo(entidad.getCargo());
        mapped.setSalario(entidad.getSalario());
        when(empleadoMapper.toDto(entidad)).thenReturn(mapped);

        Page<EmpleadoResponseDto> resultado =
                service.obtenerEmpleadosPaginados(PageRequest.of(0,1));

        assertThat(resultado.getTotalElements()).isEqualTo(1);
        EmpleadoResponseDto resp = resultado.getContent().get(0);
        assertThat(resp.getNombres()).isEqualTo("Luis");
        assertThat(resp.getEdad()).contains("40 años");
        assertThat(resp.getTiempoVinculacion()).contains("5 años");
    }
}