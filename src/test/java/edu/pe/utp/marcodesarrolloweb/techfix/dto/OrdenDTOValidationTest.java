package edu.pe.utp.marcodesarrolloweb.techfix.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de validación Jakarta para OrdenDTO.
 * Verifica que las anotaciones @NotNull, @NotBlank, @Size funcionan.
 */
class OrdenDTOValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private OrdenDTO crearOrdenValida() {
        OrdenDTO dto = new OrdenDTO();
        dto.setEquipoId(1L);
        dto.setDescripcionFalla("No enciende tras sobrecarga eléctrica");
        dto.setFecha(LocalDate.of(2026, 4, 9));
        dto.setTecnicoAsignado("Martínez, R.");
        return dto;
    }

    @Test
    @DisplayName("Orden válida: sin errores de validación")
    void ordenValida_sinErrores() {
        OrdenDTO dto = crearOrdenValida();
        Set<ConstraintViolation<OrdenDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No debe haber errores con datos válidos");
    }

    @Test
    @DisplayName("EquipoId null: dispara @NotNull")
    void equipoIdNull_disparaNotNull() {
        OrdenDTO dto = crearOrdenValida();
        dto.setEquipoId(null);

        Set<ConstraintViolation<OrdenDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
            v.getPropertyPath().toString().equals("equipoId")));
    }

    @Test
    @DisplayName("Descripción falla vacía: dispara @NotBlank")
    void descripcionVacia_disparaNotBlank() {
        OrdenDTO dto = crearOrdenValida();
        dto.setDescripcionFalla("");

        Set<ConstraintViolation<OrdenDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
            v.getPropertyPath().toString().equals("descripcionFalla")));
    }

    @Test
    @DisplayName("Fecha null: dispara @NotNull")
    void fechaNull_disparaNotNull() {
        OrdenDTO dto = crearOrdenValida();
        dto.setFecha(null);

        Set<ConstraintViolation<OrdenDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
            v.getPropertyPath().toString().equals("fecha")));
    }

    @Test
    @DisplayName("Técnico vacío: dispara @NotBlank")
    void tecnicoVacio_disparaNotBlank() {
        OrdenDTO dto = crearOrdenValida();
        dto.setTecnicoAsignado("");

        Set<ConstraintViolation<OrdenDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
            v.getPropertyPath().toString().equals("tecnicoAsignado")));
    }



    // ── Helpers ──────────────────────────────────────────────

    @Test
    @DisplayName("Helpers: getEstadoBadgeClass retorna clases correctas")
    void getEstadoBadgeClass_funciona() {
        OrdenDTO dto = new OrdenDTO();

        dto.setEstado("PENDIENTE");
        assertEquals("bg-warning text-dark", dto.getEstadoBadgeClass());

        dto.setEstado("EN_PROCESO");
        assertEquals("bg-primary", dto.getEstadoBadgeClass());

        dto.setEstado("LISTO");
        assertEquals("bg-success", dto.getEstadoBadgeClass());

        dto.setEstado("ENTREGADO");
        assertEquals("bg-info text-dark", dto.getEstadoBadgeClass());

        dto.setEstado("CANCELADO");
        assertEquals("bg-danger", dto.getEstadoBadgeClass());

        dto.setEstado(null);
        assertEquals("bg-secondary", dto.getEstadoBadgeClass());
    }



    @Test
    @DisplayName("Helpers: getFechaDisplay formatea correctamente")
    void getFechaDisplay_formateaCorrecto() {
        OrdenDTO dto = new OrdenDTO();
        dto.setFecha(LocalDate.of(2026, 4, 9));

        assertEquals("09/04/2026", dto.getFechaDisplay());
    }

    @Test
    @DisplayName("Helpers: getFechaDisplay con null retorna vacío")
    void getFechaDisplay_nullRetornaVacio() {
        OrdenDTO dto = new OrdenDTO();
        dto.setFecha(null);

        assertEquals("", dto.getFechaDisplay());
    }
}
