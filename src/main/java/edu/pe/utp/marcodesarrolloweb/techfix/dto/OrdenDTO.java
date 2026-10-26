package edu.pe.utp.marcodesarrolloweb.techfix.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OrdenDTO {

    private Long id;

    @NotNull(message = "Selecciona un equipo")
    private Long equipoId;
    private String clienteNombre;
    private String equipoNombre;

    @NotBlank(message = "La descripción de la falla es obligatoria")
    @Size(max = 500, message = "Máximo 500 caracteres")
    private String descripcionFalla;

    @NotNull(message = "La fecha es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @Size(max = 20, message = "Estado inválido")
    private String estado;

    @NotBlank(message = "El técnico asignado es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String tecnicoAsignado;
    
    private String tecnicoNombreCompleto;

    @Size(max = 500, message = "Máximo 500 caracteres")
    private String diagnostico;

    public OrdenDTO() {}

    // ── Helpers para las vistas ──────────────────────────────


    public String getEstadoBadgeClass() {
        if (estado == null) return "bg-secondary";
        return switch (estado) {
            case "PENDIENTE"  -> "bg-warning text-dark";
            case "EN_PROCESO" -> "bg-primary";
            case "LISTO"      -> "bg-success";
            case "ENTREGADO"  -> "bg-info text-dark";
            case "CANCELADO"  -> "bg-danger";
            default           -> "bg-secondary";
        };
    }


    public String getFechaDisplay() {
        return fecha != null
            ? fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }

    public String getFechaISO() {
        return fecha != null ? fecha.toString() : "";
    }

    // ── Getters y Setters ────────────────────────────────────

    public Long getId()                            { return id; }
    public void setId(Long id)                     { this.id = id; }
    public Long getEquipoId()                      { return equipoId; }
    public void setEquipoId(Long e)                { this.equipoId = e; }
    public String getClienteNombre()               { return clienteNombre; }
    public void setClienteNombre(String cn)        { this.clienteNombre = cn; }
    public String getEquipoNombre()                { return equipoNombre; }
    public void setEquipoNombre(String en)         { this.equipoNombre = en; }
    public String getDescripcionFalla()            { return descripcionFalla; }
    public void setDescripcionFalla(String d)      { this.descripcionFalla = d; }
    public LocalDate getFecha()                    { return fecha; }
    public void setFecha(LocalDate f)              { this.fecha = f; }
    public String getEstado()                      { return estado; }
    public void setEstado(String e)                { this.estado = e; }
    public String getTecnicoAsignado()             { return tecnicoAsignado; }
    public void setTecnicoAsignado(String t)       { this.tecnicoAsignado = t; }
    public String getTecnicoNombreCompleto()       { return tecnicoNombreCompleto; }
    public void setTecnicoNombreCompleto(String t) { this.tecnicoNombreCompleto = t; }
    public String getDiagnostico()                 { return diagnostico; }
    public void setDiagnostico(String d)           { this.diagnostico = d; }
}
