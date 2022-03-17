package com.tcc.manutencao_adm_ticket.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class HomeController {

    @RequestMapping("/")
    public String tela_index()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication instanceof AnonymousAuthenticationToken)
        {
            return "index";
        }
        return "home";
    }

    @RequestMapping("/index")
    public String index()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication instanceof AnonymousAuthenticationToken)
        {
            return "index";
        }
        return "home";
    }

    @RequestMapping("/login")
    public String login()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication instanceof AnonymousAuthenticationToken)
        {
            return "login";
        }
        return "home";
    }

    @RequestMapping("/home")
    public String home()
    {
        return "home";
    }


}
