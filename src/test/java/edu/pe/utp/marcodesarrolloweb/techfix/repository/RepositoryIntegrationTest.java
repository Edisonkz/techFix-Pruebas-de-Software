package edu.pe.utp.marcodesarrolloweb.techfix.repository;

import edu.pe.utp.marcodesarrolloweb.techfix.model.Cliente;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Equipo;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Orden;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Usuario;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Rol;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.RolRepository;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.UsuarioRepository;
import edu.pe.utp.marcodesarrolloweb.techfix.service.OrdenService;
import edu.pe.utp.marcodesarrolloweb.techfix.dto.OrdenDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para los repositorios JPA.
 * Usa H2 en memoria — NO toca la base de datos MySQL real.
 */
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class RepositoryIntegrationTest {

    @Autowired private ClienteRepository clienteRepo;
    @Autowired private EquipoRepository equipoRepo;
    @Autowired private OrdenRepository ordenRepo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private RolRepository rolRepo;
    @Autowired private OrdenService ordenService;

    private Cliente clienteGuardado;
    private Equipo equipoGuardado;
    private Usuario usuarioGuardado;

    @BeforeEach
    void setUp() {
        // Limpiamos y creamos datos de prueba frescos
        ordenRepo.deleteAll();
        equipoRepo.deleteAll();
        clienteRepo.deleteAll();
        usuarioRepo.deleteAll();

        clienteGuardado = clienteRepo.save(
            new Cliente("Laura García", "12345678", "laura@test.com",
                "999111222", "Activo"));

        equipoGuardado = equipoRepo.save(
            new Equipo(clienteGuardado, "Laptop", "HP", "Pavilion 15",
                "SN-001", "Registrado"));

        Usuario u = new Usuario();
        u.setUsername("admin_test");
        u.setPassword("pass");
        u.setNombreCompleto("Admin Test");
        u.setActivo(true);
        usuarioGuardado = usuarioRepo.save(u);
    }

    // ═══════ CLIENTE REPOSITORY ═══════════════════════════════

    @Test
    @DisplayName("ClienteRepo: save persiste correctamente")
    void clienteRepo_saveFunciona() {
        assertNotNull(clienteGuardado.getId());
        assertEquals("Laura García", clienteGuardado.getNombre());
    }

    @Test
    @DisplayName("ClienteRepo: findAll retorna clientes guardados")
    void clienteRepo_findAllFunciona() {
        List<Cliente> clientes = clienteRepo.findAll();
        assertFalse(clientes.isEmpty());
    }

    @Test
    @DisplayName("ClienteRepo: findById retorna el cliente correcto")
    void clienteRepo_findByIdFunciona() {
        var encontrado = clienteRepo.findById(clienteGuardado.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("12345678", encontrado.get().getDni());
    }

    @Test
    @DisplayName("ClienteRepo: existsByDni retorna true si existe")
    void clienteRepo_existsByDniFunciona() {
        assertTrue(clienteRepo.existsByDni("12345678"));
        assertFalse(clienteRepo.existsByDni("99999999"));
    }

    @Test
    @DisplayName("ClienteRepo: delete elimina correctamente")
    void clienteRepo_deleteFunciona() {
        Cliente paraEliminar = clienteRepo.save(
            new Cliente("Temp", "88888888", null, null, "Activo"));

        clienteRepo.delete(paraEliminar);

        assertFalse(clienteRepo.findById(paraEliminar.getId()).isPresent());
    }

    // ═══════ EQUIPO REPOSITORY ════════════════════════════════

    @Test
    @DisplayName("EquipoRepo: save persiste con relación a Cliente")
    void equipoRepo_saveFunciona() {
        assertNotNull(equipoGuardado.getId());
        assertEquals(clienteGuardado.getId(), equipoGuardado.getCliente().getId());
    }

    @Test
    @DisplayName("EquipoRepo: findAllConCliente trae equipos con JOIN FETCH")
    void equipoRepo_findAllConClienteFunciona() {
        List<Equipo> equipos = equipoRepo.findAllConCliente();

        assertFalse(equipos.isEmpty());
        // Verificamos que el JOIN FETCH trajo el cliente correctamente
        assertNotNull(equipos.get(0).getCliente());
        assertEquals("Laura García", equipos.get(0).getCliente().getNombre());
    }

    @Test
    @DisplayName("EquipoRepo: findByIdConCliente trae equipo con su cliente")
    void equipoRepo_findByIdConClienteFunciona() {
        var resultado = equipoRepo.findByIdConCliente(equipoGuardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals("HP", resultado.get().getMarca());
        assertNotNull(resultado.get().getCliente());
    }

    @Test
    @DisplayName("EquipoRepo: existsByNumeroSerie funciona")
    void equipoRepo_existsByNumeroSerieFunciona() {
        assertTrue(equipoRepo.existsByNumeroSerie("SN-001"));
        assertFalse(equipoRepo.existsByNumeroSerie("SN-NOEXISTE"));
    }


    // ═══════ ORDEN REPOSITORY ═════════════════════════════════

    @Test
    @DisplayName("OrdenRepo: save persiste orden con relación a Equipo")
    void ordenRepo_saveFunciona() {
        Orden orden = new Orden(
            equipoGuardado, "No enciende", LocalDate.of(2026, 4, 9),
            "PENDIENTE", "");
        orden.setUsuario(usuarioGuardado);
        orden = ordenRepo.save(orden);

        assertNotNull(orden.getId());
        assertEquals("PENDIENTE", orden.getEstado());
    }

    @Test
    @DisplayName("OrdenRepo: findAllConRelaciones trae órdenes con JOIN FETCH")
    void ordenRepo_findAllConRelacionesFunciona() {
        Orden o = new Orden(
            equipoGuardado, "Pantalla rota", LocalDate.now(),
            "EN_PROCESO", "En revisión");
        o.setUsuario(usuarioGuardado);
        ordenRepo.save(o);

        List<Orden> ordenes = ordenRepo.findAllConRelaciones();

        assertFalse(ordenes.isEmpty());
        // Verificamos que el doble JOIN FETCH trajo Equipo Y Cliente
        assertNotNull(ordenes.get(0).getEquipo());
        assertNotNull(ordenes.get(0).getEquipo().getCliente());
    }

    @Test
    @DisplayName("OrdenRepo: countByEstado cuenta correctamente")
    void ordenRepo_countByEstadoFunciona() {
        Orden o1 = new Orden(equipoGuardado, "Falla 1", LocalDate.now(), "PENDIENTE", "");
        o1.setUsuario(usuarioGuardado);
        ordenRepo.save(o1);

        Orden o2 = new Orden(equipoGuardado, "Falla 2", LocalDate.now(), "PENDIENTE", "");
        o2.setUsuario(usuarioGuardado);
        ordenRepo.save(o2);

        Orden o3 = new Orden(equipoGuardado, "Falla 3", LocalDate.now(), "LISTO", "Reparado");
        o3.setUsuario(usuarioGuardado);
        ordenRepo.save(o3);

        assertEquals(2, ordenRepo.countByEstado("PENDIENTE"));
        assertEquals(1, ordenRepo.countByEstado("LISTO"));
        assertEquals(0, ordenRepo.countByEstado("CANCELADO"));
    }


    // ═══════ INTEGRIDAD REFERENCIAL ═══════════════════════════

    @Test
    @DisplayName("Integridad: Orden tiene referencia válida a Equipo y Cliente")
    void integridad_ordenConCadenaCompleta() {
        Orden orden = new Orden(
            equipoGuardado, "Test integridad", LocalDate.now(),
            "PENDIENTE", "");
        orden.setUsuario(usuarioGuardado);
        Orden ordenGuardada = ordenRepo.save(orden);

        // Obtenemos desde la BD con JOIN FETCH
        List<Orden> ordenes = ordenRepo.findAllConRelaciones();
        Orden desdeDB = ordenes.stream()
            .filter(o -> o.getId().equals(ordenGuardada.getId()))
            .findFirst().orElseThrow();

        // Verificamos la cadena completa: Orden → Equipo → Cliente
        assertNotNull(desdeDB.getEquipo(), "La orden debe tener un equipo");
        assertNotNull(desdeDB.getEquipo().getCliente(), "El equipo debe tener un cliente");
        assertEquals("Laura García", desdeDB.getEquipo().getCliente().getNombre());
        assertEquals("HP", desdeDB.getEquipo().getMarca());
    }

    @Test
    @Transactional
    @DisplayName("Integridad: Flujo completo para registrar una orden")
    void testFlujoCompletoRegistrarOrden() {
        // 1. Crea y guarda un Usuario simulado con el rol "ROLE_TECNICO"
        Rol rolTecnico = rolRepo.findAll().stream()
            .filter(r -> "ROLE_TECNICO".equals(r.getNombre()))
            .findFirst()
            .orElseGet(() -> {
                Rol r = new Rol();
                r.setNombre("ROLE_TECNICO");
                return rolRepo.save(r);
            });

        Usuario tecnico = new Usuario();
        tecnico.setUsername("tecnico_flujo");
        tecnico.setPassword("12345");
        tecnico.setNombreCompleto("Técnico Flujo");
        tecnico.setActivo(true);
        tecnico.setRoles(java.util.Set.of(rolTecnico));
        tecnico = usuarioRepo.save(tecnico);

        // 2. Crea y guarda un Cliente de prueba
        Cliente cliente = new Cliente("Cliente Flujo", "88888888", "flujo@test.com", "999888777", "Activo");
        cliente = clienteRepo.save(cliente);

        // 3. Crea y guarda un Equipo asociado a ese cliente
        Equipo equipo = new Equipo(cliente, "Laptop", "Asus", "ROG", "SN-FLUJO", "Registrado");
        equipo = equipoRepo.save(equipo);

        // 4. Instancia un OrdenDTO y asígnale el ID del equipo, el ID del usuario (técnico) y una descripción de falla
        OrdenDTO dto = new OrdenDTO();
        dto.setEquipoId(equipo.getId());
        dto.setTecnicoAsignado(tecnico.getUsername());
        dto.setDescripcionFalla("Falla completa de encendido");
        dto.setFecha(LocalDate.now());

        // 5. Ejecuta la lógica real: ordenService.registrar(ordenDTO)
        ordenService.registrar(dto);

        // 6. Recupera la orden de la base de datos usando el OrdenRepository
        final Long equipoId = equipo.getId();
        List<Orden> ordenes = ordenRepo.findAll().stream().filter(o -> o.getEquipo().getId().equals(equipoId)).toList();
        assertFalse(ordenes.isEmpty(), "La orden debió guardarse");
        Orden ordenGuardada = ordenes.get(0);

        // 7. Aserciones (Asserts)
        assertNotNull(ordenGuardada.getId(), "La orden debe guardarse y generar un ID");
        assertEquals("PENDIENTE", ordenGuardada.getEstado(), "El estado debe ser PENDIENTE");
        assertNotNull(ordenGuardada.getFechaIngreso(), "La fechaIngreso no puede ser nula");
        assertNotNull(ordenGuardada.getFechaActualizacion(), "La fechaActualizacion no puede ser nula");
    }
}
