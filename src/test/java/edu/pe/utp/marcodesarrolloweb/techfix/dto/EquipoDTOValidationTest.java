package edu.pe.utp.marcodesarrolloweb.techfix.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de validación Jakarta para EquipoDTO.
 */
class EquipoDTOValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private EquipoDTO crearEquipoValido() {
        EquipoDTO dto = new EquipoDTO();
        dto.setClienteId(1L);
        dto.setTipo("Laptop");
        dto.setMarca("HP");
        dto.setModelo("Pavilion 15");
        dto.setNumeroSerie("SN-001");
        return dto;
    }

    @Test
    @DisplayName("Equipo válido: sin errores")
    void equipoValido_sinErrores() {
        EquipoDTO dto = crearEquipoValido();
        Set<ConstraintViolation<EquipoDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("ClienteId null: dispara @NotNull")
    void clienteIdNull_disparaNotNull() {
        EquipoDTO dto = crearEquipoValido();
        dto.setClienteId(null);

        Set<ConstraintViolation<EquipoDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
            v.getPropertyPath().toString().equals("clienteId")));
    }

    @Test
    @DisplayName("Tipo vacío: dispara @NotBlank")
    void tipoVacio_disparaNotBlank() {
        EquipoDTO dto = crearEquipoValido();
        dto.setTipo("");

        Set<ConstraintViolation<EquipoDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
            v.getPropertyPath().toString().equals("tipo")));
    }

    @Test
    @DisplayName("Marca vacía: dispara @NotBlank")
    void marcaVacia_disparaNotBlank() {
        EquipoDTO dto = crearEquipoValido();
        dto.setMarca("");

        Set<ConstraintViolation<EquipoDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
            v.getPropertyPath().toString().equals("marca")));
    }

    @Test
    @DisplayName("Modelo vacío: dispara @NotBlank")
    void modeloVacio_disparaNotBlank() {
        EquipoDTO dto = crearEquipoValido();
        dto.setModelo("");

        Set<ConstraintViolation<EquipoDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
            v.getPropertyPath().toString().equals("modelo")));
    }

    @Test
    @DisplayName("Número de serie vacío: dispara @NotBlank")
    void serieVacia_disparaNotBlank() {
        EquipoDTO dto = crearEquipoValido();
        dto.setNumeroSerie("");

        Set<ConstraintViolation<EquipoDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
            v.getPropertyPath().toString().equals("numeroSerie")));
    }

    @Test
    @DisplayName("Helpers: getDisplayInfo concatena tipo, marca y modelo")
    void getDisplayInfo_funciona() {
        EquipoDTO dto = crearEquipoValido();

        assertEquals("Laptop HP Pavilion 15", dto.getDisplayInfo());
    }

    @Test
    @DisplayName("Helpers: getSelectLabel formatea para select")
    void getSelectLabel_funciona() {
        EquipoDTO dto = crearEquipoValido();

        assertEquals("HP Pavilion 15 (SN-001)", dto.getSelectLabel());
    }
}
