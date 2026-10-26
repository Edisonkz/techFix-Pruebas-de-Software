package edu.pe.utp.marcodesarrolloweb.techfix.repository;

import edu.pe.utp.marcodesarrolloweb.techfix.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByDni(String dni);
    boolean existsByCorreo(String correo);
    boolean existsByDniAndIdNot(String dni, Long id);
    boolean existsByCorreoAndIdNot(String correo, Long id);
}
