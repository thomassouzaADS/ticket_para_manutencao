package com.tcc.manutencao_adm_ticket.controller;

import com.tcc.manutencao_adm_ticket.model.ManterUsuario;
import com.tcc.manutencao_adm_ticket.model.Usuario;
import com.tcc.manutencao_adm_ticket.repositorio.RoleRepository;
import com.tcc.manutencao_adm_ticket.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;


@RestController
@RequestMapping("/usuario")
@Controller
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleRepository roleRepository;


    //LISTAR USUARIOS
    @RequestMapping(value = "/listar_usuario", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView getUsuarios(Model model) {
        ModelAndView modelAndView = new ModelAndView("administrador/listar_usuario");
        Iterable<Usuario> usuarios = usuarioService.findAll();
        modelAndView.addObject("usuarios", usuarios);
        return modelAndView;
    }

    //BUSCAR POR TEXTO INFORMADO
    @PostMapping("/listar_usuario")
    public ModelAndView buscar_usuario(Model model, @RequestParam("texto")String texto) {
        ModelAndView modelAndView = new ModelAndView("administrador/listar_usuario");
        Iterable<Usuario> usuarios = null;
        try {
            usuarios = usuarioService.findSearch(texto);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Nada foi encontrado");
        }
        modelAndView.addObject("usuarios", usuarios);
        return modelAndView;
    }


    //CADASTRAR USUARIO
    @GetMapping("/cadastrar_usuario")
    public ModelAndView salvar(@Validated Usuario usuario)
    {
        ModelAndView modelAndView = new ModelAndView("administrador/cadastrar_usuario");
        modelAndView.addObject("usuarioObj", new Usuario());
        modelAndView.addObject("roles", roleRepository.findAll(Sort.by("nomeRole")));
        return modelAndView;
    }


    @PostMapping(value = "/cadastrar_usuario")
    public ModelAndView addUsuario(@ModelAttribute("manter_usuario") ManterUsuario manterUsuario,
                                   @ModelAttribute("usuario") Usuario usuario) {
        try {
            Usuario newUsuario = usuarioService.save(usuario, manterUsuario);
            return new ModelAndView("redirect:/usuario/listar_usuario");
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            ModelAndView modelAndView = new ModelAndView("administrador/cadastrar_usuario");
            modelAndView.addObject("errorMessage", errorMessage);
            modelAndView.addObject("usuarioObj", new Usuario());
            modelAndView.addObject("roles", roleRepository.findAll());
            return modelAndView;
        }
    }

    //EDITAR USUÁRIO
    @GetMapping(value = {"/editar_usuario/{email}"})
    public ModelAndView showEditUsuario(@PathVariable("email") String email) {
        try {
            ModelAndView modelAndView = new ModelAndView("/administrador/editar_usuario");
            modelAndView.addObject("roles", roleRepository.findAll(Sort.by("nomeRole")));
            modelAndView.addObject("usuario", usuarioService.findByEmail(email));
            return modelAndView;
        } catch (ResourceNotFoundException ex) {
            ModelAndView modelAndView = new ModelAndView("/administrador/listar_usuario");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            Iterable<Usuario> usuarios = usuarioService.findAll();
            modelAndView.addObject("usuarios", usuarios);
            return modelAndView;
        }
    }

    @PostMapping(value = {"/editar_usuario/{email}"})
    public ModelAndView updateUsuario(@PathVariable String email,
                                @ModelAttribute("manter_usuario") ManterUsuario manterUsuario,
                                @ModelAttribute("usuario") Usuario usuario) {
        try {
            usuario.setSenha(usuarioService.findByPas(email));
            usuario.setEmail(email);
            usuarioService.update(usuario, manterUsuario);
            return new ModelAndView("redirect:/usuario/listar_usuario");
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("/administrador/editar_usuario");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            modelAndView.addObject("roles", roleRepository.findAll(Sort.by("nomeRole")));
            modelAndView.addObject("usuario", usuarioService.findByEmail(email));
            return modelAndView;
        }
    }


    //EXCLUIR USUARIO
    @GetMapping(value = "/remover/{email}")
    public ModelAndView deleteUsuario(@PathVariable("email") String email, @ModelAttribute("manter_usuario")
            ManterUsuario manterUsuario) {
        try {
            usuarioService.deleteByEmail(email, manterUsuario);
            return new ModelAndView("redirect:/usuario/listar_usuario");
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("/administrador/listar_usuario");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            Iterable<Usuario> usuarios = usuarioService.findAll();
            modelAndView.addObject("usuarios", usuarios);
            return modelAndView;
        }
    }


    //ALTERAR SENHA DO USUÁRIO
    @GetMapping(value = {"/alterar_senha"})
    public ModelAndView getTrocarSenha() {
        try {
            ModelAndView modelAndView = new ModelAndView("/usuario/alterar_senha");
            return modelAndView;
        } catch (ResourceNotFoundException ex) {
            ModelAndView modelAndView = new ModelAndView("/usuario/alterar_senha");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            return modelAndView;
        }
    }

    @PostMapping(value = {"/alterar_senha"})
    public ModelAndView trocarSenha(@RequestParam String senhaAntiga, String senhaNova, String confirmaSenha,
                                    @ModelAttribute("usuario") Usuario usuario) {

        try {
            String senha = usuarioService.findByPas(SecurityContextHolder.getContext().getAuthentication().getName());
            ModelAndView modelAndView = new ModelAndView("/usuario/alterar_senha");
            if(senha.equals(senhaAntiga))
                if(senhaNova.equals(confirmaSenha))
                {
                    usuario.setSenha(senhaNova);
                    usuarioService.updateSenha(usuario);
                    return new ModelAndView("redirect:/home");
                }
                else
                {
                    String errorMessage = "Erro ao confirmar a senha";
                    modelAndView.addObject("errorMessage", errorMessage);
                    return  modelAndView;
                }
            else
            {
                String errorMessage = "Verifique a senha antiga";
                modelAndView.addObject("errorMessage", errorMessage);
                return  modelAndView;
            }
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("/usuario/alterar_senha");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            return modelAndView;
        }
    }
}

