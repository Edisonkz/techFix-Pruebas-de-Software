package edu.pe.utp.marcodesarrolloweb.techfix.mapper;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.UsuarioDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Rol;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        
        if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
            String rolesStr = usuario.getRoles().stream()
                    .map(Rol::getNombre)
                    .collect(Collectors.joining(", "));
            dto.setRol(rolesStr);
        }

        return dto;
    }

    public List<UsuarioDTO> toDTOList(List<Usuario> usuarios) {
        if (usuarios == null) {
            return null;
        }
        return usuarios.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
