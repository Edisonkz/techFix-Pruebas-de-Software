package edu.pe.utp.marcodesarrolloweb.techfix.mapper;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.EquipoDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Cliente;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Equipo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para EquipoMapper.
 * Verifica la conversión manual Entidad ↔ DTO incluyendo la relación con Cliente.
 */
class EquipoMapperTest {

    private EquipoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new EquipoMapper();
    }

    // ── toDTO ────────────────────────────────────────────────

    @Test
    @DisplayName("toDTO: convierte Equipo → EquipoDTO con datos del cliente")
    void toDTO_convierteConClienteCorrectamente() {
        Cliente cliente = new Cliente("Laura García", "12345678",
                null, null, "Activo");
        cliente.setId(1L);

        Equipo equipo = new Equipo(cliente, "Laptop", "HP", "Pavilion 15",
                "SN-001", "Registrado");
        equipo.setId(10L);

        EquipoDTO dto = mapper.toDTO(equipo);

        assertEquals(10L, dto.getId());
        assertEquals(1L, dto.getClienteId());
        assertEquals("Laura García", dto.getClienteNombre());
        assertEquals("Laptop", dto.getTipo());
        assertEquals("HP", dto.getMarca());
        assertEquals("Pavilion 15", dto.getModelo());
        assertEquals("SN-001", dto.getNumeroSerie());
        assertEquals("Registrado", dto.getEstado());
    }

    @Test
    @DisplayName("toDTO: equipo sin cliente no lanza excepción")
    void toDTO_sinClienteNoExplota() {
        Equipo equipo = new Equipo();
        equipo.setId(5L);
        equipo.setTipo("Desktop");
        equipo.setMarca("Dell");
        equipo.setModelo("Inspiron");
        equipo.setNumeroSerie("SN-X");
        equipo.setEstado("Registrado");

        EquipoDTO dto = mapper.toDTO(equipo);

        assertEquals(5L, dto.getId());
        assertNull(dto.getClienteId());
        assertNull(dto.getClienteNombre());
    }

    // ── toEntity ─────────────────────────────────────────────

    @Test
    @DisplayName("toEntity: convierte EquipoDTO → Equipo sin Cliente (el Service lo asigna)")
    void toEntity_noPoneCliente() {
        EquipoDTO dto = new EquipoDTO();
        dto.setId(3L);
        dto.setClienteId(99L);  // el mapper NO debe setear el Cliente
        dto.setTipo("Desktop");
        dto.setMarca("Lenovo");
        dto.setModelo("IdeaPad 3");
        dto.setNumeroSerie("SN-123");
        dto.setEstado("Registrado");

        Equipo entity = mapper.toEntity(dto);

        assertEquals(3L, entity.getId());
        assertNull(entity.getCliente(), "El mapper NO debe asignar Cliente — eso lo hace el Service");
        assertEquals("Desktop", entity.getTipo());
        assertEquals("Lenovo", entity.getMarca());
    }

    @Test
    @DisplayName("toEntity: estado null se convierte a 'Registrado' por defecto")
    void toEntity_estadoNullPoneRegistrado() {
        EquipoDTO dto = new EquipoDTO();
        dto.setTipo("Laptop");
        dto.setMarca("Asus");
        dto.setModelo("VivoBook");
        dto.setNumeroSerie("SN-999");

        Equipo entity = mapper.toEntity(dto);

        assertEquals("Registrado", entity.getEstado());
    }

    // ── toDTOList ────────────────────────────────────────────

    @Test
    @DisplayName("toDTOList: convierte lista correctamente")
    void toDTOList_funciona() {
        Cliente c = new Cliente("Test", "11111111", null, null, "Activo");
        c.setId(1L);
        Equipo e1 = new Equipo(c, "Laptop", "HP", "M1", "SN-A", "Registrado");
        e1.setId(1L);
        Equipo e2 = new Equipo(c, "Desktop", "Dell", "M2", "SN-B", "Registrado");
        e2.setId(2L);

        List<EquipoDTO> dtos = mapper.toDTOList(List.of(e1, e2));

        assertEquals(2, dtos.size());
        assertEquals("HP", dtos.get(0).getMarca());
        assertEquals("Dell", dtos.get(1).getMarca());
    }
}
