package edu.pe.utp.marcodesarrolloweb.techfix.dto;

public class DashboardStatsDTO {
    private Integer pendientes;
    private Integer enProceso;
    private Integer entregadas;
    private Integer tecnicosActivos;

    public DashboardStatsDTO() {}

    public DashboardStatsDTO(Integer pendientes, Integer enProceso, Integer entregadas, Integer tecnicosActivos) {
        this.pendientes = pendientes;
        this.enProceso = enProceso;
        this.entregadas = entregadas;
        this.tecnicosActivos = tecnicosActivos;
    }

    public Integer getPendientes() { return pendientes; }
    public void setPendientes(Integer pendientes) { this.pendientes = pendientes; }

    public Integer getEnProceso() { return enProceso; }
    public void setEnProceso(Integer enProceso) { this.enProceso = enProceso; }

    public Integer getEntregadas() { return entregadas; }
    public void setEntregadas(Integer entregadas) { this.entregadas = entregadas; }

    public Integer getTecnicosActivos() { return tecnicosActivos; }
    public void setTecnicosActivos(Integer tecnicosActivos) { this.tecnicosActivos = tecnicosActivos; }
}
