package edu.pe.utp.marcodesarrolloweb.techfix.service;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.ClienteDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.exception.RecursoNoEncontradoException;
import edu.pe.utp.marcodesarrolloweb.techfix.exception.ReglaNegocioException;
import edu.pe.utp.marcodesarrolloweb.techfix.mapper.ClienteMapper;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Cliente;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    public ClienteService(ClienteRepository repository, ClienteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> listarTodos() {
        return mapper.toDTOList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<ClienteDTO> listarTodosPaginados(int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by("id").descending());
        return repository.findAll(pageable).map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public ClienteDTO buscarPorId(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cliente no encontrado: " + id)));
    }

    @Transactional
    public void registrar(ClienteDTO dto) {
        if (dto.getId() == null && repository.existsByDni(dto.getDni())) {
            throw new ReglaNegocioException(
                    "Ya existe un cliente con DNI: " + dto.getDni());
        }
        if (dto.getId() == null && dto.getCorreo() != null && !dto.getCorreo().isBlank() && repository.existsByCorreo(dto.getCorreo())) {
            throw new ReglaNegocioException(
                    "Ya existe un cliente con el correo: " + dto.getCorreo());
        }

        if (dto.getCorreo() != null && dto.getCorreo().isBlank())
            dto.setCorreo(null);
        if (dto.getTelefono() != null && dto.getTelefono().isBlank())
            dto.setTelefono(null);

        repository.save(mapper.toEntity(dto));
    }

    @Transactional
    public void actualizar(ClienteDTO dto) {
        Cliente existente = repository.findById(dto.getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cliente no encontrado: " + dto.getId()));

        if (repository.existsByDniAndIdNot(dto.getDni(), dto.getId())) {
            throw new ReglaNegocioException("El DNI " + dto.getDni() + " ya está registrado en otro cliente");
        }
        if (dto.getCorreo() != null && !dto.getCorreo().isBlank() && repository.existsByCorreoAndIdNot(dto.getCorreo(), dto.getId())) {
            throw new ReglaNegocioException("El correo " + dto.getCorreo() + " ya está registrado en otro cliente");
        }

        existente.setNombre(dto.getNombre());
        existente.setDni(dto.getDni());

        String correo = (dto.getCorreo() != null && dto.getCorreo().isBlank()) ? null : dto.getCorreo();
        existente.setCorreo(correo);

        String telefono = (dto.getTelefono() != null && dto.getTelefono().isBlank()) ? null : dto.getTelefono();
        existente.setTelefono(telefono);

        if (dto.getEstado() != null) {
            existente.setEstado(dto.getEstado());
        }

        repository.save(existente);
    }
}
