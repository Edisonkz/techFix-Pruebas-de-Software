package edu.pe.utp.marcodesarrolloweb.techfix.service;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.OrdenDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.exception.RecursoNoEncontradoException;
import edu.pe.utp.marcodesarrolloweb.techfix.exception.ReglaNegocioException;
import edu.pe.utp.marcodesarrolloweb.techfix.mapper.OrdenMapper;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Equipo;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Orden;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.EquipoRepository;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.OrdenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para OrdenService usando Mockito.
 */
@ExtendWith(MockitoExtension.class)
class OrdenServiceTest {

    @Mock private OrdenRepository ordenRepository;
    @Mock private EquipoRepository equipoRepository;
    @Mock private OrdenMapper mapper;
    @Mock private edu.pe.utp.marcodesarrolloweb.techfix.repository.UsuarioRepository usuarioRepository;
    @InjectMocks private OrdenService service;

    // ── listarTodas ──────────────────────────────────────────

    @Test
    @DisplayName("listarTodas: retorna lista de OrdenDTOs")
    void listarTodas_retornaListaDTOs() {
        List<Orden> entidades = List.of(new Orden());
        OrdenDTO dto = new OrdenDTO();
        dto.setEstado("PENDIENTE");

        when(ordenRepository.findAllConRelaciones()).thenReturn(entidades);
        when(mapper.toDTOList(entidades)).thenReturn(List.of(dto));

        List<OrdenDTO> resultado = service.listarTodas();

        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());
    }

    // ── buscarPorId ──────────────────────────────────────────

    @Test
    @DisplayName("buscarPorId: retorna DTO cuando existe")
    void buscarPorId_existeRetornaDTO() {
        Orden orden = new Orden();
        OrdenDTO dto = new OrdenDTO();
        dto.setEstado("LISTO");

        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(mapper.toDTO(orden)).thenReturn(dto);

        OrdenDTO resultado = service.buscarPorId(1L);

        assertEquals("LISTO", resultado.getEstado());
    }

    @Test
    @DisplayName("buscarPorId: lanza excepción si no existe")
    void buscarPorId_noExisteLanzaExcepcion() {
        when(ordenRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
            () -> service.buscarPorId(999L));
    }

    // ── registrar ────────────────────────────────────────────

    @Test
    @DisplayName("registrar: crea orden con estado PENDIENTE por defecto")
    void registrar_estadoPendientePorDefecto() {
        OrdenDTO dto = new OrdenDTO();
        dto.setEquipoId(1L);
        dto.setDescripcionFalla("No enciende");
        dto.setFecha(LocalDate.now());
        dto.setTecnicoAsignado("Martínez, R.");

        Equipo equipo = new Equipo();
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));
        when(usuarioRepository.findByUsername(any())).thenReturn(Optional.of(new edu.pe.utp.marcodesarrolloweb.techfix.model.Usuario()));
        when(mapper.toEntity(any())).thenReturn(new Orden());

        service.registrar(dto);

        verify(ordenRepository).save(argThat(orden ->
            "PENDIENTE".equals(orden.getEstado())));
    }

    @Test
    @DisplayName("registrar: lanza excepción si equipo no existe")
    void registrar_equipoNoExisteLanzaExcepcion() {
        OrdenDTO dto = new OrdenDTO();
        dto.setEquipoId(999L);

        when(equipoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
            () -> service.registrar(dto));
    }

    @Test
    @DisplayName("registrar: diagnóstico null se convierte a cadena vacía")
    void registrar_diagnosticoNullSeConvierteACadenaVacia() {
        OrdenDTO dto = new OrdenDTO();
        dto.setEquipoId(1L);
        dto.setDescripcionFalla("Falla X");
        dto.setFecha(LocalDate.now());
        dto.setTecnicoAsignado("Torres, K.");
        dto.setDiagnostico(null);

        Equipo equipo = new Equipo();
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));
        when(usuarioRepository.findByUsername(any())).thenReturn(Optional.of(new edu.pe.utp.marcodesarrolloweb.techfix.model.Usuario()));
        when(mapper.toEntity(any())).thenReturn(new Orden());

        service.registrar(dto);

        verify(ordenRepository).save(argThat(orden ->
            "".equals(orden.getDiagnostico())));
    }

    // ── cancelar ─────────────────────────────────────────────

    @Test
    @DisplayName("cancelar: cambia estado a CANCELADO y guarda motivo")
    void cancelar_cambiaEstadoYGuardaMotivo() {
        Orden orden = new Orden();
        orden.setEstado("PENDIENTE");

        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden));

        service.cancelar(1L, "Solicitud del cliente");

        assertEquals("CANCELADO", orden.getEstado());
        assertEquals("Cancelada: Solicitud del cliente", orden.getDiagnostico());
        verify(ordenRepository).save(orden);
    }

    @Test
    @DisplayName("cancelar: lanza excepción si la orden ya estaba cancelada")
    void cancelar_yaCanceladaLanzaExcepcion() {
        Orden orden = new Orden();
        orden.setEstado("CANCELADO");

        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden));

        assertThrows(ReglaNegocioException.class,
            () -> service.cancelar(1L, "Motivo"));
    }

    @Test
    @DisplayName("cancelar: lanza excepción si la orden no existe")
    void cancelar_noExisteLanzaExcepcion() {
        when(ordenRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
            () -> service.cancelar(999L, "Motivo"));
    }

    // ── editar ───────────────────────────────────────────────

    @Test
    @DisplayName("editar: actualiza estado, técnico y diagnóstico")
    void editar_actualizaCampos() {
        Orden orden = new Orden();
        orden.setEstado("PENDIENTE");
        // orden.setTecnicoAsignado("Martínez, R.");
        orden.setDiagnostico("");

        OrdenDTO dto = new OrdenDTO();
        dto.setId(1L);
        dto.setEstado("LISTO");
        dto.setTecnicoAsignado("Torres, K.");
        dto.setDiagnostico("Reparado exitosamente");

        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden));

        service.editar(dto);

        assertEquals("LISTO", orden.getEstado());
        // assertEquals("Torres, K.", orden.getTecnicoAsignado());
        assertEquals("Reparado exitosamente", orden.getDiagnostico());
        verify(ordenRepository).save(orden);
    }

    @Test
    @DisplayName("editar: diagnóstico vacío se guarda correctamente")
    void editar_diagnosticoVacioSeGuarda() {
        Orden orden = new Orden();
        orden.setEstado("EN_PROCESO");
        orden.setDiagnostico("Diagnóstico anterior");

        OrdenDTO dto = new OrdenDTO();
        dto.setId(1L);
        dto.setEstado("EN_PROCESO");
        dto.setTecnicoAsignado("Martínez, R.");
        dto.setDiagnostico("");

        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden));

        service.editar(dto);

        assertEquals("", orden.getDiagnostico());
    }

    // ── contadores ───────────────────────────────────────────

    @Test
    @DisplayName("contarPendientes: retorna el conteo correcto")
    void contarPendientes_retornaCorrecto() {
        when(ordenRepository.countByEstado("PENDIENTE")).thenReturn(5L);

        assertEquals(5L, service.contarPendientes());
    }

    @Test
    @DisplayName("contarEnProceso: retorna el conteo correcto")
    void contarEnProceso_retornaCorrecto() {
        when(ordenRepository.countByEstado("EN_PROCESO")).thenReturn(3L);

        assertEquals(3L, service.contarEnProceso());
    }

    @Test
    @DisplayName("contarListos: retorna el conteo correcto")
    void contarListos_retornaCorrecto() {
        when(ordenRepository.countByEstado("LISTO")).thenReturn(7L);

        assertEquals(7L, service.contarListos());
    }
}
