package edu.pe.utp.marcodesarrolloweb.techfix.mapper;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.EquipoDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Equipo;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Orden;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class EquipoMapper {

    // ── Entidad → DTO ───────────────────────────────────────
    public EquipoDTO toDTO(Equipo e) {
        EquipoDTO dto = new EquipoDTO();
        dto.setId(e.getId());
        if (e.getCliente() != null) {
            dto.setClienteId(e.getCliente().getId());
            dto.setClienteNombre(e.getCliente().getNombre());
        }
        dto.setTipo(e.getTipo());
        dto.setMarca(e.getMarca());
        dto.setModelo(e.getModelo());
        dto.setNumeroSerie(e.getNumeroSerie());
        dto.setEstado(e.getEstado());
        if (e.getOrdenes() != null && !e.getOrdenes().isEmpty()) {
            dto.setOrdenesAtendidas(e.getOrdenes().size());
            
            List<Orden> ordenes = new ArrayList<>(e.getOrdenes());
            ordenes.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            if (ordenes.size() > 0) {
                Orden o1 = ordenes.get(0);
                dto.setOrdenReciente1(String.format("Orden #%03d - %s|%s|%s", 
                    o1.getId(), 
                    o1.getDescripcionFalla(), 
                    o1.getFechaIngreso().format(dtf), 
                    o1.getEstado()));
            }
            if (ordenes.size() > 1) {
                Orden o2 = ordenes.get(1);
                dto.setOrdenReciente2(String.format("Orden #%03d - %s|%s|%s", 
                    o2.getId(), 
                    o2.getDescripcionFalla(), 
                    o2.getFechaIngreso().format(dtf), 
                    o2.getEstado()));
            }
        }
        return dto;
    }

    // ── DTO → Entidad ───────────────────────────────────────
    public Equipo toEntity(EquipoDTO dto) {
        Equipo e = new Equipo();
        e.setId(dto.getId());
        e.setTipo(dto.getTipo());
        e.setMarca(dto.getMarca());
        e.setModelo(dto.getModelo());
        e.setNumeroSerie(dto.getNumeroSerie());
        e.setEstado(dto.getEstado() != null ? dto.getEstado() : "Registrado");
        return e;
    }

    // ── Lista de Entidades → Lista de DTOs ──────────────────
    public List<EquipoDTO> toDTOList(List<Equipo> list) {
        return list.stream().map(this::toDTO).toList();
    }
}
