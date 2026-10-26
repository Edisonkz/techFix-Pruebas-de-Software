package edu.pe.utp.marcodesarrolloweb.techfix.mapper;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.ClienteDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para ClienteMapper.
 * Verifica la conversión manual Entidad ↔ DTO.
 */
class ClienteMapperTest {

    private ClienteMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ClienteMapper();
    }

    // ── toDTO ────────────────────────────────────────────────

    @Test
    @DisplayName("toDTO: convierte Cliente → ClienteDTO correctamente")
    void toDTO_convierteCorrectamente() {
        Cliente cliente = new Cliente("Laura García", "12345678",
                "laura@correo.com", "999111222", "Activo");
        cliente.setId(1L);

        ClienteDTO dto = mapper.toDTO(cliente);

        assertEquals(1L, dto.getId());
        assertEquals("Laura García", dto.getNombre());
        assertEquals("12345678", dto.getDni());
        assertEquals("laura@correo.com", dto.getCorreo());
        assertEquals("999111222", dto.getTelefono());
        assertEquals("Activo", dto.getEstado());
    }

    // ── toEntity ─────────────────────────────────────────────

    @Test
    @DisplayName("toEntity: convierte ClienteDTO → Cliente correctamente")
    void toEntity_convierteCorrectamente() {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(5L);
        dto.setNombre("Carlos Mendoza");
        dto.setDni("87654321");
        dto.setCorreo("carlos@correo.com");
        dto.setTelefono("999333444");
        dto.setEstado("Activo");

        Cliente entity = mapper.toEntity(dto);

        assertEquals(5L, entity.getId());
        assertEquals("Carlos Mendoza", entity.getNombre());
        assertEquals("87654321", entity.getDni());
        assertEquals("carlos@correo.com", entity.getCorreo());
        assertEquals("999333444", entity.getTelefono());
        assertEquals("Activo", entity.getEstado());
    }

    @Test
    @DisplayName("toEntity: estado null se convierte a 'Activo' por defecto")
    void toEntity_estadoNullPoneActivo() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Test");
        dto.setDni("11111111");

        Cliente entity = mapper.toEntity(dto);

        assertEquals("Activo", entity.getEstado());
    }

    // ── toDTOList ────────────────────────────────────────────

    @Test
    @DisplayName("toDTOList: convierte lista de clientes a lista de DTOs")
    void toDTOList_convierteListaCorrectamente() {
        Cliente c1 = new Cliente("Uno", "11111111", null, null, "Activo");
        c1.setId(1L);
        Cliente c2 = new Cliente("Dos", "22222222", null, null, "Inactivo");
        c2.setId(2L);

        List<ClienteDTO> dtos = mapper.toDTOList(List.of(c1, c2));

        assertEquals(2, dtos.size());
        assertEquals("Uno", dtos.get(0).getNombre());
        assertEquals("Dos", dtos.get(1).getNombre());
    }

    @Test
    @DisplayName("toDTOList: lista vacía retorna lista vacía")
    void toDTOList_listaVaciaRetornaVacia() {
        List<ClienteDTO> dtos = mapper.toDTOList(List.of());
        assertTrue(dtos.isEmpty());
    }
}
