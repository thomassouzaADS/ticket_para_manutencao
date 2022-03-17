package com.tcc.manutencao_adm_ticket.service;

import com.tcc.manutencao_adm_ticket.exception.BadResourceException;
import com.tcc.manutencao_adm_ticket.exception.ResourceAlreadyExistsException;
import com.tcc.manutencao_adm_ticket.model.Comentario;
import com.tcc.manutencao_adm_ticket.repositorio.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

@Service
public class ComentarioService {

    @Autowired
    ComentarioRepository comentarioRepository;

    private boolean existsById(Long id) {
        return comentarioRepository.existsById(id);
    }

    public Iterable<Comentario> listarComentario(Long id) throws ResourceNotFoundException {
        Iterable<Comentario> comentarios = comentarioRepository.findComentarios(id);
        return comentarios;
    }

    public Iterable<Comentario> findComentariosManutencao(Long id) throws ResourceNotFoundException {
        Iterable<Comentario> comentariosManutencao = comentarioRepository.findComentariosManutencao(id);
        return comentariosManutencao;
    }


    public Comentario save(Comentario comentario) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(String.valueOf(comentario.getIdComentario()))) {
            if (comentario.getIdComentario() != null && existsById(comentario.getIdComentario())) {
                throw new ResourceAlreadyExistsException("Comentario com o id: " + comentario.getIdComentario() +
                        " ja existe");
            }
            comentario.setEmailUsuarioComentario(SecurityContextHolder.getContext().getAuthentication().getName());
            return comentarioRepository.save(comentario);
        }
        else {
            BadResourceException exc = new BadResourceException("Falha ao salvar o comentario");
            exc.addErrorMessage("Comentario vazio ou nulo");
            throw exc;
        }
    }
}
