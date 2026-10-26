package edu.pe.utp.marcodesarrolloweb.techfix.mapper;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.ClienteDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Cliente;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ClienteMapper {

    // ── Entidad → DTO ───────────────────────────────────────
    public ClienteDTO toDTO(Cliente c) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setDni(c.getDni());
        dto.setCorreo(c.getCorreo());
        dto.setTelefono(c.getTelefono());
        dto.setEstado(c.getEstado());
        return dto;
    }

    // ── DTO → Entidad ───────────────────────────────────────
    public Cliente toEntity(ClienteDTO dto) {
        Cliente c = new Cliente();
        c.setId(dto.getId());
        c.setNombre(dto.getNombre());
        c.setDni(dto.getDni());
        c.setCorreo(dto.getCorreo());
        c.setTelefono(dto.getTelefono());
        c.setEstado(dto.getEstado() != null ? dto.getEstado() : "Activo");
        return c;
    }

    // ── Lista de Entidades → Lista de DTOs ──────────────────
    public List<ClienteDTO> toDTOList(List<Cliente> list) {
        return list.stream().map(this::toDTO).toList();
    }
}
