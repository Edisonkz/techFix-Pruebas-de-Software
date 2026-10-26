package edu.pe.utp.marcodesarrolloweb.techfix.controller;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.ClienteDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.dto.EquipoDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.dto.OrdenDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.service.ClienteService;
import edu.pe.utp.marcodesarrolloweb.techfix.service.EquipoService;
import edu.pe.utp.marcodesarrolloweb.techfix.service.OrdenService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.stream.Collectors;
import java.util.List;
@Controller
public class TechFixController {

    private final ClienteService clienteService;
    private final EquipoService equipoService;
    private final OrdenService ordenService;
    private final edu.pe.utp.marcodesarrolloweb.techfix.service.UsuarioDetailsService usuarioDetailsService;

    public TechFixController(ClienteService clienteService, EquipoService equipoService, OrdenService ordenService, edu.pe.utp.marcodesarrolloweb.techfix.service.UsuarioDetailsService usuarioDetailsService) {
        this.clienteService = clienteService;
        this.equipoService = equipoService;
        this.ordenService = ordenService;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @GetMapping({ "/", "/inicio" })
    public String inicio(Model model) {
        model.addAttribute("activePage", "inicio");
        return "publico/index";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<OrdenDTO> ordenes = ordenService.obtenerOrdenesParaDashboard(userDetails.getUsername());
        OrdenDTO ordenAlerta = ordenService.buscarOrdenAlerta(ordenes);
        
        edu.pe.utp.marcodesarrolloweb.techfix.dto.DashboardStatsDTO stats = ordenService.obtenerEstadisticasDashboard(userDetails.getUsername());

        model.addAttribute("activePage", "dashboard");
        model.addAttribute("ordenes", ordenes);
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("equipos", equipoService.listarTodos());
        model.addAttribute("stats", stats);
        model.addAttribute("tecnicos", usuarioDetailsService.listarTecnicos());
        model.addAttribute("ordenAlerta", ordenAlerta);
        return "dashboard";
    }

    @GetMapping("/clientes")
    public String clientes(@RequestParam(name = "pagina", defaultValue = "1") int pagina, Model model) {
        model.addAttribute("activePage", "clientes");
        int pageNumber = pagina < 1 ? 0 : pagina - 1;
        model.addAttribute("clientesPage", clienteService.listarTodosPaginados(pageNumber, 10));
        model.addAttribute("cliente", new ClienteDTO());
        return "clientes";
    }

    @GetMapping("/equipos")
    public String equipos(@RequestParam(name = "pagina", defaultValue = "1") int pagina, Model model) {
        model.addAttribute("activePage", "equipos");
        int pageNumber = pagina < 1 ? 0 : pagina - 1;
        model.addAttribute("equiposPage", equipoService.listarTodosPaginados(pageNumber, 10));
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("equipo", new EquipoDTO());
        return "equipos";
    }

    @GetMapping("/ordenes")
    public String ordenes(
            @RequestParam(name = "pagina", defaultValue = "1") int pagina,
            @RequestParam(name = "buscar", required = false) String buscar,
            @RequestParam(name = "estado", required = false) String estado,
            @RequestParam(name = "tecnico", required = false) String tecnico,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
            
        model.addAttribute("activePage", "ordenes");
        
        int pageNumber = pagina < 1 ? 0 : pagina - 1;
        
        Page<OrdenDTO> ordenesPage = ordenService.listarFiltradasPaginadas(
                userDetails.getUsername(), buscar, estado, tecnico, pageNumber, 10);
        
        model.addAttribute("ordenesPage", ordenesPage);
        model.addAttribute("paramBuscar", buscar);
        model.addAttribute("paramEstado", estado);
        model.addAttribute("paramTecnico", tecnico);
        
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("equipos", equipoService.listarTodos());
        model.addAttribute("tecnicos", usuarioDetailsService.listarTecnicos());
        model.addAttribute("orden", new OrdenDTO());
        return "ordenes";
    }

    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("activePage", "contacto");
        return "publico/contacto";
    }

    @GetMapping("/error-404")
    public String error404(Model model) {
        return "error/404";
    }

    @PostMapping("/clientes/registrar")
    public String registrarCliente(
            @Valid @ModelAttribute("cliente") ClienteDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            model.addAttribute("activePage", "clientes");
            model.addAttribute("clientesPage", clienteService.listarTodosPaginados(0, 10));
            return "clientes";
        }

        clienteService.registrar(dto);
        redirectAttrs.addFlashAttribute("mensajeExito", "Cliente registrado correctamente.");
        return "redirect:/clientes";
    }

    @PostMapping("/clientes/actualizar")
    public String actualizarCliente(
            @Valid @ModelAttribute("cliente") ClienteDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttrs,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("activePage", "clientes");
            model.addAttribute("clientesPage", clienteService.listarTodosPaginados(0, 10));
            return "clientes";
        }

        clienteService.actualizar(dto);
        redirectAttrs.addFlashAttribute("mensajeExito",
                "Cliente actualizado correctamente.");
        return "redirect:/clientes";
    }

    @PostMapping("/equipos/registrar")
    public String registrarEquipo(
            @Valid @ModelAttribute("equipo") EquipoDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            model.addAttribute("activePage", "equipos");
            model.addAttribute("equiposPage", equipoService.listarTodosPaginados(0, 10));
            model.addAttribute("clientes", clienteService.listarTodos());
            return "equipos";
        }

        equipoService.registrar(dto);
        redirectAttrs.addFlashAttribute("mensajeExito", "Equipo registrado correctamente.");
        return "redirect:/equipos";
    }

    @PostMapping("/equipos/actualizar")
    public String actualizarEquipo(
            @Valid @ModelAttribute("equipo") EquipoDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            model.addAttribute("activePage", "equipos");
            model.addAttribute("equiposPage", equipoService.listarTodosPaginados(0, 10));
            model.addAttribute("clientes", clienteService.listarTodos());
            return "equipos";
        }

        equipoService.actualizar(dto);
        redirectAttrs.addFlashAttribute("mensajeExito", "Equipo actualizado correctamente.");
        return "redirect:/equipos";
    }

    @PostMapping("/ordenes/registrar")
    public String registrarOrden(
            @Valid @ModelAttribute("orden") OrdenDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            model.addAttribute("activePage", "ordenes");
            model.addAttribute("ordenesPage", ordenService.listarTodasPaginadas(0, 10));
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("equipos", equipoService.listarTodos());
            return "ordenes";
        }

        ordenService.registrar(dto);
        redirectAttrs.addFlashAttribute("mensajeExito", "Orden registrada correctamente.");
        return "redirect:/ordenes";
    }

    @PostMapping("/ordenes/cancelar")
    public String cancelarOrden(
            @RequestParam(name = "idOrden") Long idOrden,
            @RequestParam(name = "motivo", required = false, defaultValue = "") String motivo,
            RedirectAttributes redirectAttrs) {

        ordenService.cancelar(idOrden, motivo);
        redirectAttrs.addFlashAttribute("mensajeExito", "Orden cancelada.");
        return "redirect:/ordenes";
    }

    @PostMapping("/ordenes/editar")
    public String editarOrden(
            @Valid @ModelAttribute("orden") OrdenDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            model.addAttribute("activePage", "ordenes");
            model.addAttribute("ordenesPage", ordenService.listarTodasPaginadas(0, 10));
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("equipos", equipoService.listarTodos());
            return "ordenes";
        }

        ordenService.editar(dto);
        redirectAttrs.addFlashAttribute("mensajeExito", "Orden actualizada.");
        return "redirect:/ordenes";
    }


}
