package edu.pe.utp.marcodesarrolloweb.techfix.service;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.EquipoDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.exception.RecursoNoEncontradoException;
import edu.pe.utp.marcodesarrolloweb.techfix.exception.ReglaNegocioException;
import edu.pe.utp.marcodesarrolloweb.techfix.mapper.EquipoMapper;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Cliente;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Equipo;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.ClienteRepository;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.EquipoRepository;
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
 * Tests unitarios para EquipoService usando Mockito.
 */
@ExtendWith(MockitoExtension.class)
class EquipoServiceTest {

    @Mock private EquipoRepository equipoRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private EquipoMapper mapper;
    @InjectMocks private EquipoService service;

    // ── listarTodos ──────────────────────────────────────────

    @Test
    @DisplayName("listarTodos: retorna lista de EquipoDTOs")
    void listarTodos_retornaListaDTOs() {
        List<Equipo> entidades = List.of(new Equipo());
        EquipoDTO dto = new EquipoDTO();
        dto.setMarca("HP");

        when(equipoRepository.findAllConCliente()).thenReturn(entidades);
        when(mapper.toDTOList(entidades)).thenReturn(List.of(dto));

        List<EquipoDTO> resultado = service.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("HP", resultado.get(0).getMarca());
    }

    // ── buscarPorId ──────────────────────────────────────────

    @Test
    @DisplayName("buscarPorId: retorna DTO cuando existe")
    void buscarPorId_existeRetornaDTO() {
        Equipo equipo = new Equipo();
        EquipoDTO dto = new EquipoDTO();
        dto.setMarca("Dell");

        when(equipoRepository.findByIdConCliente(1L)).thenReturn(Optional.of(equipo));
        when(mapper.toDTO(equipo)).thenReturn(dto);

        EquipoDTO resultado = service.buscarPorId(1L);

        assertEquals("Dell", resultado.getMarca());
    }

    @Test
    @DisplayName("buscarPorId: lanza excepción si no existe")
    void buscarPorId_noExisteLanzaExcepcion() {
        when(equipoRepository.findByIdConCliente(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
            () -> service.buscarPorId(999L));
    }

    // ── registrar ────────────────────────────────────────────

    @Test
    @DisplayName("registrar: guarda equipo nuevo correctamente")
    void registrar_equipoNuevoOk() {
        EquipoDTO dto = new EquipoDTO();
        dto.setNumeroSerie("SN-NEW");
        dto.setClienteId(1L);

        Cliente cliente = new Cliente();
        Equipo equipo = new Equipo();

        when(equipoRepository.existsByNumeroSerie("SN-NEW")).thenReturn(false);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(mapper.toEntity(dto)).thenReturn(equipo);

        assertDoesNotThrow(() -> service.registrar(dto));
        verify(equipoRepository).save(equipo);
    }

    @Test
    @DisplayName("registrar: lanza excepción si número de serie duplicado")
    void registrar_serieDuplicadaLanzaExcepcion() {
        EquipoDTO dto = new EquipoDTO();
        dto.setNumeroSerie("SN-DUP");

        when(equipoRepository.existsByNumeroSerie("SN-DUP")).thenReturn(true);

        assertThrows(ReglaNegocioException.class,
            () -> service.registrar(dto));
        verify(equipoRepository, never()).save(any());
    }

    @Test
    @DisplayName("registrar: lanza excepción si cliente propietario no existe")
    void registrar_clienteNoExisteLanzaExcepcion() {
        EquipoDTO dto = new EquipoDTO();
        dto.setNumeroSerie("SN-X");
        dto.setClienteId(999L);

        when(equipoRepository.existsByNumeroSerie("SN-X")).thenReturn(false);
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
            () -> service.registrar(dto));
    }

    // ── actualizar ───────────────────────────────────────────

    @Test
    @DisplayName("actualizar: actualiza equipo existente preservando estado")
    void actualizar_preservaEstado() {
        EquipoDTO dto = new EquipoDTO();
        dto.setId(1L);
        dto.setClienteId(1L);
        dto.setMarca("Lenovo");

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Equipo existente = new Equipo();
        existente.setEstado("En reparación");
        existente.setCliente(cliente);

        when(equipoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        service.actualizar(dto);

        // Verificamos que el estado original se preservó
        assertEquals("En reparación", existente.getEstado());
        assertEquals("Lenovo", existente.getMarca());
        assertEquals(cliente, existente.getCliente());
        verify(equipoRepository).save(existente);
    }

    @Test
    @DisplayName("actualizar: lanza excepción si equipo no existe")
    void actualizar_noExisteLanzaExcepcion() {
        EquipoDTO dto = new EquipoDTO();
        dto.setId(999L);

        when(equipoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
            () -> service.actualizar(dto));
    }
}
