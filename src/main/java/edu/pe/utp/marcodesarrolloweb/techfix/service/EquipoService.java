package edu.pe.utp.marcodesarrolloweb.techfix.service;

import edu.pe.utp.marcodesarrolloweb.techfix.dto.EquipoDTO;
import edu.pe.utp.marcodesarrolloweb.techfix.exception.RecursoNoEncontradoException;
import edu.pe.utp.marcodesarrolloweb.techfix.exception.ReglaNegocioException;
import edu.pe.utp.marcodesarrolloweb.techfix.mapper.EquipoMapper;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Cliente;
import edu.pe.utp.marcodesarrolloweb.techfix.model.Equipo;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.ClienteRepository;
import edu.pe.utp.marcodesarrolloweb.techfix.repository.EquipoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EquipoService {

    private final EquipoRepository equipoRepository;
    private final ClienteRepository clienteRepository;
    private final EquipoMapper mapper;

    public EquipoService(EquipoRepository equipoRepository, ClienteRepository clienteRepository, EquipoMapper mapper) {
        this.equipoRepository = equipoRepository;
        this.clienteRepository = clienteRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<EquipoDTO> listarTodos() {
        return mapper.toDTOList(equipoRepository.findAllConCliente());
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<EquipoDTO> listarTodosPaginados(int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return equipoRepository.findAllConClientePaginado(pageable).map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public EquipoDTO buscarPorId(Long id) {
        return mapper.toDTO(equipoRepository.findByIdConCliente(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Equipo no encontrado: " + id)));
    }

    @Transactional
    public void registrar(EquipoDTO dto) {
        if (dto.getId() == null && equipoRepository.existsByNumeroSerie(dto.getNumeroSerie())) {
            throw new ReglaNegocioException(
                "Ya existe un equipo con el número de serie: " + dto.getNumeroSerie());
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Cliente propietario no encontrado"));

        Equipo equipo = mapper.toEntity(dto);
        equipo.setCliente(cliente);
        equipoRepository.save(equipo);
    }
    
    @Transactional
    public void actualizar(EquipoDTO dto) {
        Equipo existente = equipoRepository.findById(dto.getId())
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Equipo no encontrado: " + dto.getId()));

        if (equipoRepository.existsByNumeroSerieAndIdNot(dto.getNumeroSerie(), dto.getId())) {
            throw new ReglaNegocioException(
                "El número de serie " + dto.getNumeroSerie() + " ya pertenece a otro equipo");
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Cliente propietario no encontrado"));

        if (!existente.getCliente().getId().equals(cliente.getId())) {
            if (existente.getOrdenes() != null && !existente.getOrdenes().isEmpty()) {
                throw new ReglaNegocioException(
                    "No se puede cambiar el propietario de un equipo que ya tiene un historial de órdenes de servicio");
            }
        }

        existente.setCliente(cliente);
        existente.setTipo(dto.getTipo());
        existente.setMarca(dto.getMarca());
        existente.setModelo(dto.getModelo());
        existente.setNumeroSerie(dto.getNumeroSerie());

        
        equipoRepository.save(existente);
    }
}
