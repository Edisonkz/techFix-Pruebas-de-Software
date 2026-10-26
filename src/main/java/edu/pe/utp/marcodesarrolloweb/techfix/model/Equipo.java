package edu.pe.utp.marcodesarrolloweb.techfix.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne — muchos equipos pertenecen a un cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false, length = 20)
    private String tipo;            // "Laptop", "Desktop"

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(nullable = false, unique = true, length = 30)
    private String numeroSerie;

    @Column(nullable = false, length = 20)
    private String estado;          // "Registrado", "En reparación", "Entregado"

    // @OneToMany — un equipo tiene muchas órdenes
    @OneToMany(mappedBy = "equipo", fetch = FetchType.LAZY)
    private List<Orden> ordenes = new ArrayList<>();

    public Equipo() {}

    public Equipo(Cliente cliente, String tipo, String marca,
                  String modelo, String numeroSerie, String estado) {
        this.cliente     = cliente;
        this.tipo        = tipo;
        this.marca       = marca;
        this.modelo      = modelo;
        this.numeroSerie = numeroSerie;
        this.estado      = estado;
    }

    // ── Helpers ──────────────────────────────────────────────
    public String getDisplayInfo() {
        return (tipo != null ? tipo : "") + " "
             + (marca != null ? marca : "") + " "
             + (modelo != null ? modelo : "");
    }

    // ── Getters y Setters ────────────────────────────────────
    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }
    public Cliente getCliente()                { return cliente; }
    public void setCliente(Cliente c)          { this.cliente = c; }
    public String getTipo()                    { return tipo; }
    public void setTipo(String t)              { this.tipo = t; }
    public String getMarca()                   { return marca; }
    public void setMarca(String m)             { this.marca = m; }
    public String getModelo()                  { return modelo; }
    public void setModelo(String m)            { this.modelo = m; }
    public String getNumeroSerie()             { return numeroSerie; }
    public void setNumeroSerie(String ns)      { this.numeroSerie = ns; }
    public String getEstado()                  { return estado; }
    public void setEstado(String e)            { this.estado = e; }
    public List<Orden> getOrdenes()            { return ordenes; }
    public void setOrdenes(List<Orden> o)      { this.ordenes = o; }
}
