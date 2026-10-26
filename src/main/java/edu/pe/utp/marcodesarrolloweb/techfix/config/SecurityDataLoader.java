package edu.pe.utp.marcodesarrolloweb.techfix.config;

import edu.pe.utp.marcodesarrolloweb.techfix.model.*;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class SecurityDataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ClienteRepository clienteRepository;
    private final EquipoRepository equipoRepository;
    private final OrdenRepository ordenRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityDataLoader(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              ClienteRepository clienteRepository,
                              EquipoRepository equipoRepository,
                              OrdenRepository ordenRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.clienteRepository = clienteRepository;
        this.equipoRepository = equipoRepository;
        this.ordenRepository = ordenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Inicializar Roles
        if (rolRepository.count() == 0) {
            Rol adminRol = new Rol();
            adminRol.setNombre("ROLE_ADMIN");
            rolRepository.save(adminRol);

            Rol tecnicoRol = new Rol();
            tecnicoRol.setNombre("ROLE_TECNICO");
            rolRepository.save(tecnicoRol);
        }

        Rol adminRol = rolRepository.findByNombre("ROLE_ADMIN").orElse(null);
        Rol tecnicoRol = rolRepository.findByNombre("ROLE_TECNICO").orElse(null);

        // 2. Inicializar Usuarios (3 usuarios)
        if (usuarioRepository.count() == 0 && adminRol != null && tecnicoRol != null) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNombreCompleto("Jorge Ramírez (Administrador)");
            admin.setActivo(true);
            admin.setRoles(Set.of(adminRol));
            usuarioRepository.save(admin);

            Usuario tecnico = new Usuario();
            tecnico.setUsername("tecnico");
            tecnico.setPassword(passwordEncoder.encode("tecnico123"));
            tecnico.setNombreCompleto("Juan Pérez (Técnico Especialista)");
            tecnico.setActivo(true);
            tecnico.setRoles(Set.of(tecnicoRol));
            usuarioRepository.save(tecnico);

            Usuario supervisor = new Usuario();
            supervisor.setUsername("supervisor");
            supervisor.setPassword(passwordEncoder.encode("admin123"));
            supervisor.setNombreCompleto("Carlos Supervisor QA");
            supervisor.setActivo(true);
            supervisor.setRoles(Set.of(adminRol, tecnicoRol));
            usuarioRepository.save(supervisor);
        }

        Usuario usuarioAdmin = usuarioRepository.findByUsername("admin").orElse(null);
        Usuario usuarioTecnico = usuarioRepository.findByUsername("tecnico").orElse(null);

        // 3. Inicializar Clientes de prueba (8 clientes vinculados al Word y RF-07)
        if (clienteRepository.count() == 0) {
            Cliente c1 = new Cliente("Carlos Torres", "71234567", "carlos.torres@gmail.com", "987654321", "Activo");
            Cliente c2 = new Cliente("Jorge Ramírez", "72345678", "jorge.ramirez@techfix.pe", "981234567", "Activo");
            Cliente c3 = new Cliente("Ana Gómez Mendoza", "73456789", "ana.gomez@empresa.pe", "992345678", "Activo");
            Cliente c4 = new Cliente("Luis Huamán Prado", "74567890", "luis.huaman@outlook.com", "973456789", "Activo");
            Cliente c5 = new Cliente("María Quispe Castro", "75678901", "maria.quispe@gmail.com", "964567890", "Activo");
            Cliente c6 = new Cliente("Pedro Fernández Silva", "76789012", "pedro.fs@gmail.com", "955678901", "Activo");
            Cliente c7 = new Cliente("Rosa Díaz Paredes", "77890123", "rosa.diaz@gmail.com", "946789012", "Activo");
            Cliente c8 = new Cliente("Miguel Romero Salas", "78901234", "miguel.romero@tech.pe", "937890123", "Activo");

            clienteRepository.saveAll(List.of(c1, c2, c3, c4, c5, c6, c7, c8));
        }

        // 4. Inicializar Equipos de prueba (10 equipos vinculados a RF-08)
        if (equipoRepository.count() == 0 && clienteRepository.count() > 0) {
            List<Cliente> clientes = clienteRepository.findAll();
            Cliente c1 = clientes.get(0);
            Cliente c2 = clientes.get(1);
            Cliente c3 = clientes.get(2);
            Cliente c4 = clientes.get(3);
            Cliente c5 = clientes.get(4);
            Cliente c6 = clientes.get(5);
            Cliente c7 = clientes.get(6);
            Cliente c8 = clientes.get(7);

            Equipo e1 = new Equipo(c1, "Laptop", "Dell", "Inspiron 15 3501", "SN-DL-99", "En reparación");
            Equipo e2 = new Equipo(c2, "Laptop", "HP", "Pavilion 14-dv000", "SN-HP-14", "En reparación");
            Equipo e3 = new Equipo(c3, "Laptop", "Lenovo", "ThinkPad E14 Gen 2", "SN-LN-14", "Listo para entrega");
            Equipo e4 = new Equipo(c4, "Desktop", "ASUS", "ROG Strix G15DH", "SN-AS-01", "En reparación");
            Equipo e5 = new Equipo(c5, "Laptop", "Acer", "Swift 3 SF314-59", "SN-AC-03", "Registrado");
            Equipo e6 = new Equipo(c6, "Desktop", "HP", "ProDesk 400 G6 MT", "SN-HP-40", "Listo para entrega");
            Equipo e7 = new Equipo(c7, "Laptop", "Apple", "MacBook Air M1 2020", "SN-AP-M1", "Cancelado / Devuelto");
            Equipo e8 = new Equipo(c8, "Laptop", "Dell", "Latitude 5420", "SN-DL-54", "Entregado");
            Equipo e9 = new Equipo(c1, "Desktop", "Lenovo", "IdeaCentre 3 07ADA05", "SN-LN-IC", "En reparación");
            Equipo e10 = new Equipo(c2, "Laptop", "Toshiba", "Tecra A50-EC", "SN-TB-01", "Registrado");

            equipoRepository.saveAll(List.of(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10));
        }

        // 5. Inicializar Órdenes de Servicio (10 órdenes vinculadas a RF-03 / RF-05 y estados)
        if (ordenRepository.count() == 0 && equipoRepository.count() > 0) {
            List<Equipo> equipos = equipoRepository.findAll();
            LocalDate hoy = LocalDate.now();

            // Orden 1: PENDIENTE (CP-ORD-01)
            Orden o1 = new Orden(equipos.get(0), "Pantalla azul recurrente (BSOD) tras actualización del sistema operativo", hoy.minusDays(5), "PENDIENTE", "");
            o1.setUsuario(usuarioAdmin);
            o1.setFechaActualizacion(LocalDateTime.now().minusDays(5));

            // Orden 2: EN_PROCESO (CP-ORD-02)
            Orden o2 = new Orden(equipos.get(1), "No enciende y parpadea LED de batería al conectar cargador", hoy.minusDays(4), "EN_PROCESO", "Falla detectada en etapa de alimentación de placa madre; requiere microsoldadura");
            o2.setUsuario(usuarioTecnico);
            o2.setFechaActualizacion(LocalDateTime.now().minusDays(3));

            // Orden 3: LISTO
            Orden o3 = new Orden(equipos.get(2), "Lentitud extrema y temperatura superior a 85°C en reposo", hoy.minusDays(3), "LISTO", "Mantenimiento general térmico realizado, reemplazo de pasta disipadora y clonación a SSD NVMe 512GB");
            o3.setUsuario(usuarioTecnico);
            o3.setFechaActualizacion(LocalDateTime.now().minusDays(1));

            // Orden 4: EN_PROCESO
            Orden o4 = new Orden(equipos.get(3), "Ventiladores de GPU emiten ruido metálico y throttling térmico", hoy.minusDays(3), "EN_PROCESO", "Limpieza y lubricación de rodamientos de ventilador disipador");
            o4.setUsuario(usuarioTecnico);
            o4.setFechaActualizacion(LocalDateTime.now().minusDays(2));

            // Orden 5: PENDIENTE
            Orden o5 = new Orden(equipos.get(4), "Teclado no responde teclas espacio y enter tras caída accidental de café", hoy.minusDays(2), "PENDIENTE", "");
            o5.setUsuario(usuarioAdmin);
            o5.setFechaActualizacion(LocalDateTime.now().minusDays(2));

            // Orden 6: LISTO
            Orden o6 = new Orden(equipos.get(5), "Fuente de poder no suministra voltaje a placa base ni enciende ventilador", hoy.minusDays(2), "LISTO", "Reemplazo de fuente de poder ATX 500W certificada 80 Plus");
            o6.setUsuario(usuarioTecnico);
            o6.setFechaActualizacion(LocalDateTime.now().minusDays(1));

            // Orden 7: CANCELADO (CP-ORD-03)
            Orden o7 = new Orden(equipos.get(6), "Pantalla Retina con líneas verticales moradas tras golpe leve", hoy.minusDays(2), "CANCELADO", "Cancelación formal: Cliente no aprueba presupuesto por elevado costo de pantalla original Apple");
            o7.setUsuario(usuarioAdmin);
            o7.setFechaActualizacion(LocalDateTime.now().minusDays(1));

            // Orden 8: ENTREGADO
            Orden o8 = new Orden(equipos.get(7), "Batería dura menos de 20 minutos desconectada de corriente", hoy.minusDays(6), "ENTREGADO", "Instalación de batería original Dell 4 celdas 53Wh y calibración de ciclos");
            o8.setUsuario(usuarioTecnico);
            o8.setFechaActualizacion(LocalDateTime.now().minusDays(1));

            // Orden 9: EN_PROCESO
            Orden o9 = new Orden(equipos.get(8), "Infección por ransomware en archivos contables y ventanas emergentes", hoy.minusDays(1), "EN_PROCESO", "Aislamiento de unidad, escaneo offline y restauración de respaldo limpio");
            o9.setUsuario(usuarioTecnico);
            o9.setFechaActualizacion(LocalDateTime.now().minusHours(12));

            // Orden 10: PENDIENTE
            Orden o10 = new Orden(equipos.get(9), "Bisagra izquierda rota que bloquea la apertura de la pantalla", hoy, "PENDIENTE", "");
            o10.setUsuario(usuarioAdmin);
            o10.setFechaActualizacion(LocalDateTime.now());

            ordenRepository.saveAll(List.of(o1, o2, o3, o4, o5, o6, o7, o8, o9, o10));
        }
    }
}
