package com.tcc.manutencao_adm_ticket.controller;

import com.tcc.manutencao_adm_ticket.model.Role;
import com.tcc.manutencao_adm_ticket.repositorio.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RestController
@Controller
public class RoleController {
    @Autowired
    RoleRepository roleRepository;

    @RequestMapping(value = "/cadastrar_usuario", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView buscar(){
        ModelAndView andView = new ModelAndView("administrador/cadastrar_usuario");
        Iterable<Role> role = roleRepository.findAll();
        andView.addObject("roles", role);
        return andView;
    }

}