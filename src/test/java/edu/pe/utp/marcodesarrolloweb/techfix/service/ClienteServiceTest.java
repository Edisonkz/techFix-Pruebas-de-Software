package edu.pe.utp.marcodesarrolloweb.techfix.service;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.ClienteDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.exception.RecursoNoEncontradoException;
import edu.pe.utp.marcodesarrolloweb.techfix.exception.ReglaNegocioException;
import edu.pe.utp.marcodesarrolloweb.techfix.mapper.ClienteMapper;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Cliente;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ClienteService usando Mockito.
 * NO toca la base de datos real — el repositorio se simula.
 */
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock private ClienteRepository repository;
    @Mock private ClienteMapper mapper;
    @InjectMocks private ClienteService service;

    // ── listarTodos ──────────────────────────────────────────

    @Test
    @DisplayName("listarTodos: retorna lista de DTOs")
    void listarTodos_retornaListaDTOs() {
        List<Cliente> entidades = List.of(
            new Cliente("Laura", "11111111", null, null, "Activo"));
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Laura");

        when(repository.findAll()).thenReturn(entidades);
        when(mapper.toDTOList(entidades)).thenReturn(List.of(dto));

        List<ClienteDTO> resultado = service.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Laura", resultado.get(0).getNombre());
        verify(repository).findAll();
    }

    // ── buscarPorId ──────────────────────────────────────────

    @Test
    @DisplayName("buscarPorId: retorna DTO cuando el cliente existe")
    void buscarPorId_clienteExiste() {
        Cliente cliente = new Cliente("Carlos", "22222222", null, null, "Activo");
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Carlos");

        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        when(mapper.toDTO(cliente)).thenReturn(dto);

        ClienteDTO resultado = service.buscarPorId(1L);

        assertEquals("Carlos", resultado.getNombre());
    }

    @Test
    @DisplayName("buscarPorId: lanza RecursoNoEncontradoException si no existe")
    void buscarPorId_noExisteLanzaExcepcion() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
            () -> service.buscarPorId(999L));
    }

    // ── registrar ────────────────────────────────────────────

    @Test
    @DisplayName("registrar: guarda cliente nuevo correctamente")
    void registrar_clienteNuevoOk() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Miguel");
        dto.setDni("33333333");

        when(repository.existsByDni("33333333")).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(new Cliente());

        assertDoesNotThrow(() -> service.registrar(dto));
        verify(repository).save(any(Cliente.class));
    }

    @Test
    @DisplayName("registrar: lanza ReglaNegocioException si DNI duplicado")
    void registrar_dniDuplicadoLanzaExcepcion() {
        ClienteDTO dto = new ClienteDTO();
        dto.setDni("11111111");

        when(repository.existsByDni("11111111")).thenReturn(true);

        assertThrows(ReglaNegocioException.class,
            () -> service.registrar(dto));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("registrar: correo en blanco se convierte a null")
    void registrar_correoEnBlancoSeConvierteANull() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Test");
        dto.setDni("44444444");
        dto.setCorreo("   ");
        dto.setTelefono("");

        when(repository.existsByDni("44444444")).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(new Cliente());

        service.registrar(dto);

        assertNull(dto.getCorreo());
        assertNull(dto.getTelefono());
    }

    // ── actualizar ───────────────────────────────────────────

    @Test
    @DisplayName("actualizar: actualiza cliente existente correctamente")
    void actualizar_clienteExistenteOk() {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(1L);
        dto.setNombre("Laura Modificada");
        dto.setDni("12345678");

        Cliente existente = new Cliente();
        existente.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        assertDoesNotThrow(() -> service.actualizar(dto));
        
        assertEquals("Laura Modificada", existente.getNombre());
        assertEquals("12345678", existente.getDni());
        verify(repository).save(existente);
    }

    @Test
    @DisplayName("actualizar: lanza excepción si el cliente no existe")
    void actualizar_noExisteLanzaExcepcion() {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(999L);

        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
            () -> service.actualizar(dto));
    }
}
