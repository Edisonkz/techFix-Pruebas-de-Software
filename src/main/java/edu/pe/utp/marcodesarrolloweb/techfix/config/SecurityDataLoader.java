package edu.pe.utp.marcodesarrolloweb.techfix.config;

import edu.pe.utp.marcodesarrolloweb.techfix.model.Rol;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Usuario;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.RolRepository;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SecurityDataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityDataLoader(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (rolRepository.count() == 0) {
            Rol adminRol = new Rol();
            adminRol.setNombre("ROLE_ADMIN");
            rolRepository.save(adminRol);

            Rol tecnicoRol = new Rol();
            tecnicoRol.setNombre("ROLE_TECNICO");
            rolRepository.save(tecnicoRol);
        }

        if (usuarioRepository.count() == 0) {
            Rol adminRol = rolRepository.findByNombre("ROLE_ADMIN").orElseThrow();
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNombreCompleto("Administrador del Sistema");
            admin.setActivo(true);
            admin.setRoles(Set.of(adminRol));
            usuarioRepository.save(admin);

            Rol tecnicoRol = rolRepository.findByNombre("ROLE_TECNICO").orElseThrow();
            Usuario tecnico = new Usuario();
            tecnico.setUsername("tecnico");
            tecnico.setPassword(passwordEncoder.encode("tecnico123"));
            tecnico.setNombreCompleto("Técnico Principal");
            tecnico.setActivo(true);
            tecnico.setRoles(Set.of(tecnicoRol));
            usuarioRepository.save(tecnico);
        }
    }
}
