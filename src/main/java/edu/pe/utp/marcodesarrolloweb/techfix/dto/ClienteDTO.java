package edu.pe.utp.marcodesarrolloweb.techfix.dto;

import jakarta.validation.constraints.*;

public class ClienteDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos numéricos")
    private String dni;

    @Pattern(regexp = "^$|^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$",
             message = "Ingresa un correo electrónico válido")
    private String correo;

    @Pattern(regexp = "^$|^\\d+$", message = "El teléfono solo debe contener números")
    @Size(max = 15, message = "El teléfono no puede superar los 15 caracteres")
    private String telefono;

    @Size(max = 20, message = "Estado inválido")
    private String estado;

    public ClienteDTO() {}

    // ── Helpers para las vistas ──────────────────────────────

    public String getIniciales() {
        if (nombre == null || nombre.trim().isEmpty()) return "";
        String[] parts = nombre.trim().split("\\s+");
        if (parts.length >= 2) {
            return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
        }
        return nombre.length() >= 2 ? nombre.substring(0, 2).toUpperCase() : nombre.toUpperCase();
    }

    public String getEstadoBadgeClass() {
        return "Activo".equals(estado) ? "bg-success" : "bg-secondary";
    }

    // ── Getters y Setters ────────────────────────────────────

    public Long getId()                  { return id; }
    public void setId(Long id)           { this.id = id; }
    public String getNombre()            { return nombre; }
    public void setNombre(String n)      { this.nombre = n; }
    public String getDni()               { return dni; }
    public void setDni(String d)         { this.dni = d; }
    public String getCorreo()            { return correo; }
    public void setCorreo(String c)      { this.correo = c; }
    public String getTelefono()          { return telefono; }
    public void setTelefono(String t)    { this.telefono = t; }
    public String getEstado()            { return estado; }
    public void setEstado(String e)      { this.estado = e; }
}
