package edu.pe.utp.marcodesarrolloweb.techfix.mapper;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.OrdenDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Orden;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class OrdenMapper {

    // ── DTO → Entidad ───────────────────────────────────────
    public Orden toEntity(OrdenDTO dto) {
        Orden entity = new Orden();
        entity.setId(dto.getId());
        entity.setDescripcionFalla(dto.getDescripcionFalla());
        entity.setFechaIngreso(dto.getFecha());
        entity.setEstado(dto.getEstado());
        entity.setDiagnostico(dto.getDiagnostico());
        // prioridad y tecnicoAsignado no se mapean a la entidad (se ignoran)
        return entity;
    }

    // ── Entidad → DTO ───────────────────────────────────────
    public OrdenDTO toDTO(Orden o) {
        OrdenDTO dto = new OrdenDTO();
        dto.setId(o.getId());
        if (o.getEquipo() != null) {
            dto.setEquipoId(o.getEquipo().getId());
            dto.setEquipoNombre(o.getEquipo().getDisplayInfo());
            if (o.getEquipo().getCliente() != null) {
                dto.setClienteNombre(o.getEquipo().getCliente().getNombre());
            }
        }
        dto.setDescripcionFalla(o.getDescripcionFalla());
        dto.setFecha(o.getFechaIngreso());
        dto.setEstado(o.getEstado());
        // La entidad ya no tiene prioridad ni tecnicoAsignado como Strings,
        // pero podemos obtener el técnico del usuario asociado si es necesario.
        if (o.getUsuario() != null) {
            dto.setTecnicoAsignado(o.getUsuario().getUsername());
            dto.setTecnicoNombreCompleto(o.getUsuario().getNombreCompleto());
        }
        dto.setDiagnostico(o.getDiagnostico());
        return dto;
    }

    // ── Lista de Entidades → Lista de DTOs ──────────────────
    public List<OrdenDTO> toDTOList(List<Orden> list) {
        return list.stream().map(this::toDTO).toList();
    }
}
