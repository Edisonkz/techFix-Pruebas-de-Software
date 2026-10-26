package edu.pe.utp.marcodesarrolloweb.techfix.mapper;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.OrdenDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Cliente;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Equipo;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Orden;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para OrdenMapper.
 * Verifica la conversión Entidad → DTO (no existe toEntity en este mapper).
 */
class OrdenMapperTest {

    private OrdenMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OrdenMapper();
    }

    @Test
    @DisplayName("toDTO: convierte Orden → OrdenDTO con relaciones completas")
    void toDTO_convierteConRelacionesCompletas() {
        Cliente cliente = new Cliente("Laura García", "12345678",
                null, null, "Activo");
        cliente.setId(1L);

        Equipo equipo = new Equipo(cliente, "Laptop", "HP", "Pavilion 15",
                "SN-001", "Registrado");
        equipo.setId(10L);

        Orden orden = new Orden(equipo, "No enciende", LocalDate.of(2026, 4, 9),
                "PENDIENTE", "Sin diagnóstico");
        orden.setId(100L);

        OrdenDTO dto = mapper.toDTO(orden);

        assertEquals(100L, dto.getId());
        assertEquals(10L, dto.getEquipoId());
        assertEquals("Laura García", dto.getClienteNombre());
        assertNotNull(dto.getEquipoNombre());
        assertEquals("No enciende", dto.getDescripcionFalla());
        assertEquals(LocalDate.of(2026, 4, 9), dto.getFecha());
        assertEquals("PENDIENTE", dto.getEstado());
        // assertEquals("Normal", dto.getPrioridad());
        // assertEquals("Martínez, R.", dto.getTecnicoAsignado());
        assertEquals("Sin diagnóstico", dto.getDiagnostico());
    }

    @Test
    @DisplayName("toDTO: orden con equipo sin cliente no lanza excepción")
    void toDTO_equipoSinClienteNoExplota() {
        Equipo equipo = new Equipo();
        equipo.setId(5L);
        equipo.setTipo("Desktop");
        equipo.setMarca("Dell");
        equipo.setModelo("Inspiron");
        equipo.setNumeroSerie("SN-X");
        equipo.setEstado("Registrado");

        Orden orden = new Orden(equipo, "Pantalla rota",
                LocalDate.now(), "EN_PROCESO", "");
        orden.setId(50L);

        OrdenDTO dto = mapper.toDTO(orden);

        assertEquals(50L, dto.getId());
        assertEquals(5L, dto.getEquipoId());
        assertNull(dto.getClienteNombre());
    }

    @Test
    @DisplayName("toDTOList: convierte lista correctamente")
    void toDTOList_funciona() {
        Cliente c = new Cliente("Test", "11111111", null, null, "Activo");
        c.setId(1L);
        Equipo eq = new Equipo(c, "Laptop", "HP", "M1", "SN-A", "Registrado");
        eq.setId(1L);

        Orden o1 = new Orden(eq, "Falla 1", LocalDate.now(),
                "PENDIENTE", "");
        o1.setId(1L);
        Orden o2 = new Orden(eq, "Falla 2", LocalDate.now(),
                "LISTO", "Reparado");
        o2.setId(2L);

        List<OrdenDTO> dtos = mapper.toDTOList(List.of(o1, o2));

        assertEquals(2, dtos.size());
        assertEquals("Falla 1", dtos.get(0).getDescripcionFalla());
        assertEquals("Falla 2", dtos.get(1).getDescripcionFalla());
    }

    @Test
    @DisplayName("toDTOList: lista vacía retorna lista vacía")
    void toDTOList_listaVaciaRetornaVacia() {
        assertTrue(mapper.toDTOList(List.of()).isEmpty());
    }
}
