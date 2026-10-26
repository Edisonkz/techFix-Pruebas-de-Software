package edu.pe.utp.marcodesarrolloweb.techfix.repository;

import edu.pe.utp.marcodesarrolloweb.techfix.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    
    @org.springframework.data.jpa.repository.Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.nombre = :rol")
    java.util.List<Usuario> findByRolesNombre(@org.springframework.data.repository.query.Param("rol") String rol);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(u) FROM Usuario u JOIN u.roles r WHERE r.nombre = :rol")
    long countByRolesNombre(@org.springframework.data.repository.query.Param("rol") String rol);
}
