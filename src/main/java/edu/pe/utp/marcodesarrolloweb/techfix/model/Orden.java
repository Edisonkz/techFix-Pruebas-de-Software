package edu.pe.utp.marcodesarrolloweb.techfix.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes_servicio")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne — muchas órdenes pertenecen a un equipo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 500)
    private String descripcionFalla;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(nullable = false, length = 20)
    private String estado;          // "PENDIENTE", "EN_PROCESO", "LISTO", "ENTREGADO", "CANCELADO"

    @Column(length = 500)
    private String diagnostico;

    public Orden() {}

    public Orden(Equipo equipo, String descripcionFalla, LocalDate fechaIngreso,
                 String estado, String diagnostico) {
        this.equipo           = equipo;
        this.descripcionFalla = descripcionFalla;
        this.fechaIngreso     = fechaIngreso;
        this.estado           = estado;
        this.diagnostico      = diagnostico;
        this.fechaActualizacion = LocalDateTime.now();
    }

    // ── Helpers ──────────────────────────────────────────────
    public boolean isCancelado() { return "CANCELADO".equals(estado); }

    // ── Getters y Setters ────────────────────────────────────
    public Long getId()                            { return id; }
    public void setId(Long id)                     { this.id = id; }
    public Equipo getEquipo()                      { return equipo; }
    public void setEquipo(Equipo eq)               { this.equipo = eq; }
    public Usuario getUsuario()                    { return usuario; }
    public void setUsuario(Usuario u)              { this.usuario = u; }
    public String getDescripcionFalla()            { return descripcionFalla; }
    public void setDescripcionFalla(String d)      { this.descripcionFalla = d; }
    public LocalDate getFechaIngreso()             { return fechaIngreso; }
    public void setFechaIngreso(LocalDate f)       { this.fechaIngreso = f; }
    public LocalDateTime getFechaActualizacion()   { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime f) { this.fechaActualizacion = f; }
    public String getEstado()                      { return estado; }
    public void setEstado(String e)                { this.estado = e; }
    public String getDiagnostico()                 { return diagnostico; }
    public void setDiagnostico(String d)           { this.diagnostico = d; }
}
