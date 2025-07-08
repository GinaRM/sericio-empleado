package com.gina.servicio_empleado.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gina.servicio_empleado.controller.EmpleadoController;
import com.gina.servicio_empleado.dto.EmpleadoDto;
import com.gina.servicio_empleado.dto.EmpleadoResponseDto;
import com.gina.servicio_empleado.service.EmpleadoProcessorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(EmpleadoController.class)
public class EmpleadoControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper json;

    @MockBean
    EmpleadoProcessorService service;

    @Test
    void postProcesarEmpleado_ok() throws Exception {
        EmpleadoDto dto = new EmpleadoDto();
        dto.setNombres("Marta");
        dto.setApellidos("Ruiz");
        dto.setTipoDocumento("CC");
        dto.setNumeroDocumento("789");
        dto.setFechaNacimiento(LocalDate.now().minusYears(25));
        dto.setFechaVinculacion(LocalDate.now().minusYears(1));
        dto.setCargo("QA");
        dto.setSalario(3000.0);

        EmpleadoResponseDto resp = new EmpleadoResponseDto(
                "Marta", "Ruiz", "CC", "789",
                dto.getFechaNacimiento(), dto.getFechaVinculacion(),
                "QA", 3000.0,
                "25 años, 0 meses, 0 días",
                "1 años, 0 meses, 0 días"
        );
        when(service.procesarEmpleado(any(EmpleadoDto.class))).thenReturn(resp);

        mvc.perform(post("/api/empleado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.edad").value("25 años, 0 meses, 0 días"))
                .andExpect(jsonPath("$.tiempoVinculacion").value("1 años, 0 meses, 0 días"));
    }

    @Test
    void getAllPaginado_ok() throws Exception {
        EmpleadoResponseDto resp = new EmpleadoResponseDto(
                "X", "Y", "CC", "1",
                LocalDate.now().minusYears(30),
                LocalDate.now().minusYears(5),
                "DEV", 4000.0,
                "30 años, 0 meses, 0 días",
                "5 años, 0 meses, 0 días"
        );
        Page<EmpleadoResponseDto> page = new PageImpl<>(List.of(resp), PageRequest.of(0,10), 1);
        when(service.obtenerEmpleadosPaginados(any(Pageable.class))).thenReturn(page);

        mvc.perform(get("/api/empleado/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].tiempoVinculacion").value("5 años, 0 meses, 0 días"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
