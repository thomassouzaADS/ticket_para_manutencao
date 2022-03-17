package com.tcc.manutencao_adm_ticket.service;

import com.tcc.manutencao_adm_ticket.exception.BadResourceException;
import com.tcc.manutencao_adm_ticket.exception.ResourceAlreadyExistsException;
import com.tcc.manutencao_adm_ticket.model.ManterUsuario;
import com.tcc.manutencao_adm_ticket.model.Usuario;
import com.tcc.manutencao_adm_ticket.repositorio.ManterUsuarioRepository;
import com.tcc.manutencao_adm_ticket.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Collection;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ManterUsuarioRepository manterUsuarioRepository;

    private boolean existsByEmail(String email) {
        return usuarioRepository.existsById(email);
    }

    public Usuario findByEmail(String email) throws ResourceNotFoundException {
        Usuario usuario = new Usuario();
        usuario = usuarioRepository.findById(email).orElse(null);
        if (usuario==null) {
            throw new ResourceNotFoundException("Não foi possivel encontrar o email: " + email);
        }
        else return usuario;
    }

    public Usuario findByEmailManutencao(String email) throws ResourceNotFoundException {
        Usuario usuario = new Usuario();
        usuario = usuarioRepository.findById(email).orElse(null);
        if (usuario==null) {
            throw new ResourceNotFoundException("Não foi possivel encontrar o email: " + email);
        }
        else return usuario;
    }

    public Iterable<Usuario> findAll() {
        Iterable<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios;
    }

    public Iterable<Usuario> findSearch(String texto){
        Iterable<Usuario> usuarios = usuarioRepository.findSearch(texto);
        if (usuarios==null) {
            throw new ResourceNotFoundException("Não foi possivel encontrar a seguinte informação: " + texto);
        }
        else return usuarios;
    }

    public String findByPas(String email) {
        String senha = usuarioRepository.findByPass(email);
        return senha;
    }

    public Usuario save(Usuario usuario, ManterUsuario manterUsuario) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(usuario.getEmail())) {
            if (usuario.getEmail() != null && existsByEmail(usuario.getEmail())) {
                throw new ResourceAlreadyExistsException("Usuario com o email: " + usuario.getEmail() +
                        " ja existe");
            }
            manterUsuario.setEmailUser(SecurityContextHolder.getContext().getAuthentication().getName());
            manterUsuario.setAlteracao("POST");
            manterUsuario.setEmailAlterado(usuario.getEmail());
            manterUsuarioRepository.save(manterUsuario);
            return usuarioRepository.save(usuario);
        }
        else {
            BadResourceException exc = new BadResourceException("Falha ao salvar o usuario");
            exc.addErrorMessage("Usuario vazio ou nulo");
            throw exc;
        }
    }

    public void update(Usuario usuario, ManterUsuario manterUsuario)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(usuario.getEmail())) {
            if (!existsByEmail(usuario.getEmail())) {
                throw new ResourceNotFoundException("Não foi possivel localizar este usuario: " + usuario.getEmail());
            }
            manterUsuario.setEmailUser(SecurityContextHolder.getContext().getAuthentication().getName());
            manterUsuario.setAlteracao("UPDATE");
            manterUsuario.setEmailAlterado(usuario.getEmail());
            manterUsuarioRepository.save(manterUsuario);
            usuarioRepository.save(usuario);
        }
        else {
            BadResourceException exc = new BadResourceException("Falha ao editar o usuario");
            exc.addErrorMessage("Usuario vazio ou nulo");
            throw exc;
        }
    }

    public void updateSenha(Usuario usuario)
            throws BadResourceException, ResourceNotFoundException {

        usuario.setEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!StringUtils.isEmpty(usuario.getEmail())) {
            if (!existsByEmail(usuario.getEmail())) {
                throw new ResourceNotFoundException("Não foi possivel alterar a senha deste usuario: " + usuario.getEmail());
            }
            Usuario userUp = findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            usuario.setNomeUsuario(userUp.getNomeUsuario());
            usuario.setTelefone(userUp.getTelefone());
            usuario.setEmpresa(userUp.getEmpresa());
            usuario.setCidade(userUp.getCidade());
            usuario.setEstado(userUp.getEstado());
            usuario.setEmail(userUp.getEmail());
            usuario.setRole(userUp.getRole());
            usuarioRepository.save(usuario);
        }
        else {
            BadResourceException exc = new BadResourceException("Falha ao editar senha do usuario");
            exc.addErrorMessage("Campo senha vazio ou nulo");
            throw exc;
        }
    }

    public void deleteByEmail(String email, ManterUsuario manterUsuario) throws BadResourceException, ResourceNotFoundException {
        if(!email.equals(SecurityContextHolder.getContext().getAuthentication().getName()))
        {
            if (!existsByEmail(email)) {
                throw new ResourceNotFoundException("Não foi possivel encontrar um usuario com o email: " + email);
            }else{
                manterUsuario.setEmailUser(SecurityContextHolder.getContext().getAuthentication().getName());
                manterUsuario.setAlteracao("DELETE");
                manterUsuario.setEmailAlterado(email);
                manterUsuarioRepository.save(manterUsuario);
                usuarioRepository.deleteByEmail(email);
            }
        }else {
            BadResourceException exc = new BadResourceException("Falha ao tentar deletar o usuario");
            exc.addErrorMessage("Não pode se deletar");
            throw exc;
        }
    }
}
