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
 * Tests de validación Jakarta para ClienteDTO.
 * Verifica que las anotaciones @NotBlank, @Pattern, @Size funcionan.
 */
class ClienteDTOValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private ClienteDTO crearClienteValido() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Laura García");
        dto.setDni("12345678");
        dto.setCorreo("laura@correo.com");
        dto.setTelefono("999111222");
        dto.setEstado("Activo");
        return dto;
    }

    @Test
    @DisplayName("Cliente válido: sin errores de validación")
    void clienteValido_sinErrores() {
        ClienteDTO dto = crearClienteValido();
        Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No debe haber errores con datos válidos");
    }

    @Test
    @DisplayName("Nombre vacío: dispara @NotBlank")
    void nombreVacio_disparaNotBlank() {
        ClienteDTO dto = crearClienteValido();
        dto.setNombre("");

        Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
            v.getPropertyPath().toString().equals("nombre")));
    }

    @Test
    @DisplayName("DNI con letras: dispara @Pattern")
    void dniConLetras_disparaPattern() {
        ClienteDTO dto = crearClienteValido();
        dto.setDni("1234ABCD");

        Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
            v.getPropertyPath().toString().equals("dni")));
    }

    @Test
    @DisplayName("DNI con menos de 8 dígitos: dispara @Pattern")
    void dniCorto_disparaPattern() {
        ClienteDTO dto = crearClienteValido();
        dto.setDni("1234");

        Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Correo inválido: dispara @Pattern")
    void correoInvalido_disparaPattern() {
        ClienteDTO dto = crearClienteValido();
        dto.setCorreo("no-es-un-correo");

        Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
            v.getPropertyPath().toString().equals("correo")));
    }

    @Test
    @DisplayName("Correo vacío: NO dispara error (es opcional)")
    void correoVacio_noDisparaError() {
        ClienteDTO dto = crearClienteValido();
        dto.setCorreo("");

        Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(dto);

        boolean tieneErrorCorreo = violations.stream().anyMatch(v ->
            v.getPropertyPath().toString().equals("correo"));
        assertFalse(tieneErrorCorreo, "Correo vacío no debe fallar por @Pattern");
    }

    @Test
    @DisplayName("Helpers: getIniciales funciona correctamente")
    void getIniciales_funciona() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Laura García");

        assertEquals("LG", dto.getIniciales());
    }

    @Test
    @DisplayName("Helpers: getEstadoBadgeClass retorna clase correcta")
    void getEstadoBadgeClass_funciona() {
        ClienteDTO dto = new ClienteDTO();
        dto.setEstado("Activo");
        assertEquals("bg-success", dto.getEstadoBadgeClass());

        dto.setEstado("Inactivo");
        assertEquals("bg-secondary", dto.getEstadoBadgeClass());
    }
}
