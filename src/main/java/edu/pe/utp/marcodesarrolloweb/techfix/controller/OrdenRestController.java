package edu.pe.utp.marcodesarrolloweb.techfix.controller;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.OrdenDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.service.OrdenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenRestController {

    private final OrdenService ordenService;

    public OrdenRestController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @GetMapping
    public List<OrdenDTO> listarTodas() {
        return ordenService.listarTodas();
    }
}
