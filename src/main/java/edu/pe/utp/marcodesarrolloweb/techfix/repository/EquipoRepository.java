package edu.pe.utp.marcodesarrolloweb.techfix.repository;

import edu.pe.utp.marcodesarrolloweb.techfix.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    boolean existsByNumeroSerie(String numeroSerie);
    boolean existsByNumeroSerieAndIdNot(String numeroSerie, Long id);


    @Query("SELECT e FROM Equipo e JOIN FETCH e.cliente WHERE e.id = :id")
    Optional<Equipo> findByIdConCliente(@Param("id") Long id);

    @Query("SELECT e FROM Equipo e JOIN FETCH e.cliente ORDER BY e.id DESC")
    List<Equipo> findAllConCliente();

    @Query(value = "SELECT e FROM Equipo e JOIN FETCH e.cliente ORDER BY e.id DESC",
           countQuery = "SELECT count(e) FROM Equipo e")
    org.springframework.data.domain.Page<Equipo> findAllConClientePaginado(org.springframework.data.domain.Pageable pageable);


}
