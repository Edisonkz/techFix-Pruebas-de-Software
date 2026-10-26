package edu.pe.utp.marcodesarrolloweb.techfix.service;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.UsuarioDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.mapper.UsuarioMapper;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Usuario;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (usuario.getActivo() == null || !usuario.getActivo()) {
            throw new UsernameNotFoundException("El usuario está inactivo");
        }

        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getRoles().stream()
                        .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                        .collect(Collectors.toList())
        );
    }

    public java.util.List<UsuarioDTO> listarTecnicos() {
        return usuarioMapper.toDTOList(usuarioRepository.findByRolesNombre("ROLE_TECNICO"));
    }

    public long contarTecnicos() {
        return usuarioRepository.countByRolesNombre("ROLE_TECNICO");
    }

    public boolean esTecnico(org.springframework.security.core.userdetails.UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TECNICO"));
    }
}
