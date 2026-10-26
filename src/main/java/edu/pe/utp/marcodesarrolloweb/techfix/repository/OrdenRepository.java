package edu.pe.utp.marcodesarrolloweb.techfix.repository;

import edu.pe.utp.marcodesarrolloweb.techfix.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {



    @Query("SELECT DISTINCT o FROM Orden o JOIN FETCH o.equipo e JOIN FETCH e.cliente ORDER BY o.id DESC")
    List<Orden> findAllConRelaciones();


    @Query(value = "SELECT DISTINCT o FROM Orden o JOIN FETCH o.equipo e JOIN FETCH e.cliente ORDER BY o.id DESC",
           countQuery = "SELECT count(o) FROM Orden o")
    org.springframework.data.domain.Page<Orden> findAllConRelacionesPaginado(org.springframework.data.domain.Pageable pageable);


    List<Orden> findByUsuarioUsername(String username);

    long countByEstado(String estado);
}
