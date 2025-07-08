# servicio-empleado

Servicio REST en Java/Spring Boot para procesar datos de empleados y reenviarlos a un cliente SOAP simulado, almacenando la información en MySQL.

---

## 📋 Descripción

Este microservicio expone dos endpoints HTTP que:

1. **Validan** los datos de un empleado (fechas, no nulos, rango de edad 18–100 años).
2. **Llaman** a un cliente SOAP (simulado en `EmpleadoSoapClientImpl`) para persistir la entidad en MySQL.
3. **Devuelven** un JSON con los datos del empleado y dos campos calculados:
   - **edad**: años, meses y días desde la fecha de nacimiento.
   - **tiempoVinculacion**: años, meses y días desde la fecha de vinculación.

La lógica de cliente SOAP está desacoplada mediante la interfaz\
`EmpleadoSoapClient`, para poder sustituirla por una implementación real basada en WSDL.

---

## 🚀 Tecnologías

- Java 17
- Spring Boot 3.5.3
- Spring Web, Spring Data JPA, Bean Validation
- MySQL (runtime)
- Maven
- JUnit 5 + Mockito (tests)
- JaCoCo (cobertura)

---

## 📦 Estructura de paquetes

```
com.gina.servicio_empleado
├─ controller
│   └─ EmpleadoController.java      # Exposición de endpoints
├─ dto
│   ├─ EmpleadoDto.java             # Request DTO
│   └─ EmpleadoResponseDto.java     # Response DTO
├─ exception
│   └─ EmpleadoDuplicadoException.java
├─ model
│   └─ EmpleadoEntity.java          # JPA @Entity
├─ repository
│   └─ EmpleadoRepository.java      # extends JpaRepository<EmpleadoEntity, Integer>
├─ service
│   ├─ EmpleadoProcessorService.java  # Lógica de negocio y validaciones
│   ├─ EmpleadoSoapClient.java        # Interfaz cliente SOAP
│   └─ EmpleadoSoapClientImpl.java    # Implementación simulada de SOAP + save()
├─ util
│   └─ EmpleadoMapper.java          # Mapea Entity ↔ DTO
└─ application.properties           # Configuración de MySQL, JPA, etc.
```

---

## 📡 Endpoints

### 1. Procesar Empleado

```
GET  /api/empleado
```

**Query params obligatorios**:

| Nombre           | Tipo   | Ejemplo         | Validación                         |
| ---------------- | ------ | --------------- | ---------------------------------- |
| nombres          | String | `Juan`          | No vacío                           |
| apellidos        | String | `Pérez`         | No vacío                           |
| tipoDocumento    | String | `CC`            | No vacío                           |
| numeroDocumento  | String | `12345678`      | No vacío, único                    |
| fechaNacimiento  | Date   | `1985-07-12`    | ISO (YYYY-MM-DD), ≤ hoy, ≥ 18 años |
| fechaVinculacion | Date   | `2010-03-01`    | ISO, ≤ hoy                         |
| cargo            | String | `Desarrollador` | No vacío                           |
| salario          | Double | `3500.00`       | > 0                                |

**Respuesta** (200 OK):

```json
{
  "nombres": "Juan",
  "apellidos": "Pérez",
  "tipoDocumento": "CC",
  "numeroDocumento": "12345678",
  "fechaNacimiento": "1985-07-12",
  "fechaVinculacion": "2010-03-01",
  "cargo": "Desarrollador",
  "salario": 3500.0,
  "edad": "39 años, 0 meses, 26 días",
  "tiempoVinculacion": "14 años, 4 meses, 7 días"
}
```

**Errores posibles**:

- `400 Bad Request` → validaciones de campo (formato, ausentes, rango de edad, etc.).
- `409 Conflict` → `numeroDocumento` duplicado.

---

### 2. Listar Empleados Paginados

```
GET  /api/empleado/paginado
```

**Query params** (`page`, `size`, `sort`, etc. de Spring Data Pageable).

**Respuesta** (200 OK):

```json
{
  "content": [ /* lista de EmpleadoResponseDto */ ],
  "pageable": { /* metadata */ },
  "totalElements": 42,
  "totalPages": 5,
  "number": 0,
  "size": 10,
  "last": false,
  "first": true
}
```

---

## 🧠 Decisiones de diseño

- **Separación de responsabilidades**

  - Controller → expone endpoints.
  - Service → validaciones, cálculos y coordinación.
  - Mapper → DTO ↔ Entity.
  - Cliente SOAP → interfaz desacoplada.

- **Validación centralizada**

  - `@Valid` + Bean Validation para campos.
  - Reglas de negocio en `EmpleadoProcessorService`.

- **Manejo de excepciones**

  - `EmpleadoDuplicadoException` → `409 Conflict`.
  - `IllegalArgumentException` → `400 Bad Request`.

- **DTOs vs Entidades**

  - No exponer JPA Entity en la API.
  - Request/Response a través de DTOs.

- **Cliente SOAP simulado**

  - `EmpleadoSoapClientImpl` usa `empleadoRepository.save()`.
  - Fácil reemplazo por cliente real.

- **Tests & Cobertura**

  - JUnit 5 + Mockito para cubrir validaciones y flujos.
  

---

## ⚙️ Cómo ejecutar

1. Crear BD MySQL (p. ej. `servicio_empleado`).

2. Configurar `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/servicio_empleado
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=TU_PASS
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Ejecutar:

   ```bash
   mvn clean spring-boot:run
   ```



---

## 🖊️ Autora

**Gina Rodríguez**\
[LinkedIn](https://www.linkedin.com/in/tu-perfil)

