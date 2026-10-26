package edu.pe.utp.marcodesarrolloweb.techfix.dto;

import jakarta.validation.constraints.*;

public class EquipoDTO {

    private Long id;

    @NotNull(message = "Selecciona un cliente propietario")
    private Long clienteId;
    private String clienteNombre;

    @NotBlank(message = "Selecciona el tipo de equipo")
    private String tipo;

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 50, message = "La marca no debe exceder 50 caracteres")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 100, message = "El modelo no debe exceder 100 caracteres")
    private String modelo;

    @NotBlank(message = "El número de serie es obligatorio")
    @Size(max = 30, message = "El número de serie no debe exceder 30 caracteres")
    private String numeroSerie;

    @Size(max = 20, message = "Estado inválido")
    private String estado;

    private Integer ordenesAtendidas;
    private String ordenReciente1;
    private String ordenReciente2;

    public EquipoDTO() {}

    // ── Helpers para las vistas ──────────────────────────────

    public String getDisplayInfo() {
        return (tipo != null ? tipo : "") + " "
             + (marca != null ? marca : "") + " "
             + (modelo != null ? modelo : "");
    }

    public String getSelectLabel() {
        return marca + " " + modelo + " (" + numeroSerie + ")";
    }

    // ── Getters y Setters ────────────────────────────────────

    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }
    public Long getClienteId()                   { return clienteId; }
    public void setClienteId(Long c)             { this.clienteId = c; }
    public String getClienteNombre()             { return clienteNombre; }
    public void setClienteNombre(String cn)      { this.clienteNombre = cn; }
    public String getTipo()                      { return tipo; }
    public void setTipo(String t)                { this.tipo = t; }
    public String getMarca()                     { return marca; }
    public void setMarca(String m)               { this.marca = m; }
    public String getModelo()                    { return modelo; }
    public void setModelo(String m)              { this.modelo = m; }
    public String getNumeroSerie()               { return numeroSerie; }
    public void setNumeroSerie(String ns)        { this.numeroSerie = ns; }
    public String getEstado()                    { return estado; }
    public void setEstado(String e)              { this.estado = e; }
    public Integer getOrdenesAtendidas()             { return ordenesAtendidas; }
    public void setOrdenesAtendidas(Integer o)       { this.ordenesAtendidas = o; }
    public String getOrdenReciente1()            { return ordenReciente1; }
    public void setOrdenReciente1(String o)      { this.ordenReciente1 = o; }
    public String getOrdenReciente2()            { return ordenReciente2; }
    public void setOrdenReciente2(String o)      { this.ordenReciente2 = o; }
}
