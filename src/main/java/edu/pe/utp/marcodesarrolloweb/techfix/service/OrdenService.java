package edu.pe.utp.marcodesarrolloweb.techfix.service;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.OrdenDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.exception.RecursoNoEncontradoException;
import edu.pe.utp.marcodesarrolloweb.techfix.exception.ReglaNegocioException;
import edu.pe.utp.marcodesarrolloweb.techfix.mapper.OrdenMapper;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Equipo;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Orden;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.EquipoRepository;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.OrdenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.pe.utp.marcodesarrolloweb.techfix.model.Usuario;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.UsuarioRepository;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final EquipoRepository equipoRepository;
    private final OrdenMapper mapper;
    private final UsuarioRepository usuarioRepository;

    public OrdenService(OrdenRepository ordenRepository, EquipoRepository equipoRepository, OrdenMapper mapper, UsuarioRepository usuarioRepository) {
        this.ordenRepository = ordenRepository;
        this.equipoRepository = equipoRepository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<OrdenDTO> listarTodas() {
        return mapper.toDTOList(ordenRepository.findAllConRelaciones());
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<OrdenDTO> listarTodasPaginadas(int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return ordenRepository.findAllConRelacionesPaginado(pageable).map(mapper::toDTO);
    }


    @Transactional(readOnly = true)
    public List<OrdenDTO> listarPorTecnico(String username) {
        return mapper.toDTOList(ordenRepository.findByUsuarioUsername(username));
    }

    @Transactional(readOnly = true)
    public List<OrdenDTO> obtenerOrdenesParaDashboard(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
            
        boolean isTecnico = usuario.getRoles().stream()
            .anyMatch(r -> "ROLE_TECNICO".equals(r.getNombre()));
            
        if (isTecnico) {
            return listarPorTecnico(username);
        }
        return listarTodas();
    }

    @Transactional(readOnly = true)
    public edu.pe.utp.marcodesarrolloweb.techfix.dto.DashboardStatsDTO obtenerEstadisticasDashboard(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
            
        boolean isTecnico = usuario.getRoles().stream()
            .anyMatch(r -> "ROLE_TECNICO".equals(r.getNombre()));

        List<OrdenDTO> ordenes = isTecnico ? listarPorTecnico(username) : listarTodas();
        
        Integer pendientes = Integer.valueOf((int) ordenes.stream().filter(o -> "PENDIENTE".equals(o.getEstado())).count());
        Integer enProceso = Integer.valueOf((int) ordenes.stream().filter(o -> "EN_PROCESO".equals(o.getEstado())).count());
        Integer entregadas = Integer.valueOf((int) ordenes.stream().filter(o -> "ENTREGADO".equals(o.getEstado())).count());
        Integer tecnicosActivos = Integer.valueOf((int) usuarioRepository.countByRolesNombre("ROLE_TECNICO"));

        return new edu.pe.utp.marcodesarrolloweb.techfix.dto.DashboardStatsDTO(pendientes, enProceso, entregadas, tecnicosActivos);
    }

    @Transactional(readOnly = true)
    public OrdenDTO buscarOrdenAlerta(List<OrdenDTO> ordenes) {
        return ordenes.stream()
            .filter(o -> "PENDIENTE".equals(o.getEstado()) || "EN_PROCESO".equals(o.getEstado()))
            .findFirst().orElse(null);
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<OrdenDTO> listarFiltradasPaginadas(String username,
            String buscar, String estado, String tecnico, int page, int size) {
        
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
            
        boolean isTecnico = usuario.getRoles().stream()
            .anyMatch(r -> "ROLE_TECNICO".equals(r.getNombre()));

        List<OrdenDTO> todas = isTecnico 
            ? listarPorTecnico(username) 
            : listarTodas();

        List<OrdenDTO> filtradas = todas.stream().filter(o -> {
            boolean matchBuscar = true;
            if (buscar != null && !buscar.trim().isEmpty()) {
                String q = buscar.toLowerCase().trim();
                matchBuscar = (o.getEquipoNombre() != null && o.getEquipoNombre().toLowerCase().contains(q)) ||
                              (o.getClienteNombre() != null && o.getClienteNombre().toLowerCase().contains(q)) ||
                              (o.getDescripcionFalla() != null && o.getDescripcionFalla().toLowerCase().contains(q));
            }
            boolean matchEstado = (estado == null || estado.trim().isEmpty()) || estado.equals(o.getEstado());
            boolean matchTecnico = (tecnico == null || tecnico.trim().isEmpty()) || tecnico.equals(o.getTecnicoAsignado());
            return matchBuscar && matchEstado && matchTecnico;
        }).collect(java.util.stream.Collectors.toList());

        int startItem = page * size;
        List<OrdenDTO> listaPagina = filtradas.size() < startItem 
            ? List.of() 
            : filtradas.subList(startItem, Math.min(startItem + size, filtradas.size()));

        return new org.springframework.data.domain.PageImpl<>(
            listaPagina, 
            org.springframework.data.domain.PageRequest.of(page, size), 
            filtradas.size()
        );
    }

    @Transactional(readOnly = true)
    public OrdenDTO buscarPorId(Long id) {
        return mapper.toDTO(ordenRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Orden no encontrada: " + id)));
    }

    @Transactional
    public void registrar(OrdenDTO dto) {
        Equipo equipo = equipoRepository.findById(dto.getEquipoId())
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Equipo no encontrado"));

        String username = dto.getTecnicoAsignado();
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new ReglaNegocioException("Técnico no encontrado"));

        Orden orden = mapper.toEntity(dto);
        orden.setEquipo(equipo);
        orden.setUsuario(usuario);
        
        if (orden.getFechaIngreso() == null) {
            orden.setFechaIngreso(LocalDate.now());
        }
        if (orden.getDiagnostico() == null) {
            orden.setDiagnostico("");
        }
        
        orden.setEstado("PENDIENTE");
        orden.setFechaActualizacion(LocalDateTime.now());
        
        ordenRepository.save(orden);
    }

    @Transactional
    public void cancelar(Long id, String motivo) {
        Orden orden = ordenRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Orden no encontrada: " + id));

        if ("CANCELADO".equals(orden.getEstado()) || "ENTREGADO".equals(orden.getEstado())) {
            throw new ReglaNegocioException("La orden ya se encuentra en un estado terminal (Cancelada/Entregada) y no admite modificaciones.");
        }

        orden.setEstado("CANCELADO");
        if (motivo != null && !motivo.isBlank()) {
            orden.setDiagnostico("Cancelada: " + motivo);
        }
        ordenRepository.save(orden);
    }

    @Transactional
    public void editar(OrdenDTO dto) {
        if (dto.getEstado() == null || dto.getEstado().isBlank()) {
            throw new ReglaNegocioException("Debes seleccionar un estado.");
        }

        Orden orden = ordenRepository.findById(dto.getId())
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Orden no encontrada: " + dto.getId()));
        
        if ("CANCELADO".equals(orden.getEstado()) || "ENTREGADO".equals(orden.getEstado())) {
            throw new ReglaNegocioException("La orden ya se encuentra en un estado terminal (Cancelada/Entregada) y no admite modificaciones.");
        }

        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            orden.setEstado(dto.getEstado());
        }
        orden.setFechaActualizacion(LocalDateTime.now());
        if (dto.getDiagnostico() != null) {
            orden.setDiagnostico(dto.getDiagnostico());
        }
        
        ordenRepository.save(orden);
    }
    
    @Transactional(readOnly = true)
    public long contarPendientes() {
        return ordenRepository.countByEstado("PENDIENTE");
    }

    @Transactional(readOnly = true)
    public long contarEnProceso() {
        return ordenRepository.countByEstado("EN_PROCESO");
    }

    @Transactional(readOnly = true)
    public long contarListos() {
        return ordenRepository.countByEstado("LISTO");
    }
}
