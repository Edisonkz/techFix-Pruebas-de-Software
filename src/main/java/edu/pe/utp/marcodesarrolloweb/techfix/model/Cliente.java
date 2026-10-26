package edu.pe.utp.marcodesarrolloweb.techfix.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Column(length = 100)
    private String correo;

    @Column(length = 15)
    private String telefono;

    @Column(nullable = false, length = 20)
    private String estado;   // "Activo", "Inactivo"

    // @OneToMany — un cliente tiene muchos equipos
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Equipo> equipos = new ArrayList<>();

    public Cliente() {}

    public Cliente(String nombre, String dni, String correo,
                   String telefono, String estado) {
        this.nombre   = nombre;
        this.dni      = dni;
        this.correo   = correo;
        this.telefono = telefono;
        this.estado   = estado;
    }

    // ── Getters y Setters ────────────────────────────────────
    public Long getId()                      { return id; }
    public void setId(Long id)               { this.id = id; }
    public String getNombre()                { return nombre; }
    public void setNombre(String n)          { this.nombre = n; }
    public String getDni()                   { return dni; }
    public void setDni(String d)             { this.dni = d; }
    public String getCorreo()                { return correo; }
    public void setCorreo(String c)          { this.correo = c; }
    public String getTelefono()              { return telefono; }
    public void setTelefono(String t)        { this.telefono = t; }
    public String getEstado()                { return estado; }
    public void setEstado(String e)          { this.estado = e; }
    public List<Equipo> getEquipos()         { return equipos; }
    public void setEquipos(List<Equipo> eq)  { this.equipos = eq; }
}
