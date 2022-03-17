package com.tcc.manutencao_adm_ticket.controller;

import com.tcc.manutencao_adm_ticket.model.Comentario;
import com.tcc.manutencao_adm_ticket.model.Ticket;
import com.tcc.manutencao_adm_ticket.service.ComentarioService;
import com.tcc.manutencao_adm_ticket.service.TicketService;
import com.tcc.manutencao_adm_ticket.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/ticket")
@Controller
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ComentarioService comentarioService;

    //LISTAR TICKET USER
    @RequestMapping(value = "/listar_ticket", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView getTicket(Model model) {
        ModelAndView modelAndView = new ModelAndView("requerente/listar_ticket");
        Iterable<Ticket> ticket = ticketService.listarTicket(SecurityContextHolder.getContext().getAuthentication().getName());
        modelAndView.addObject("tickets", ticket);
        return modelAndView;
    }

    //BUSCAR POR TEXTO INFORMADO
    @PostMapping("/listar_ticket")
    public ModelAndView buscar_ticket(Model model, @RequestParam("texto")String texto) {
        ModelAndView modelAndView = new ModelAndView("requerente/listar_ticket");
        Iterable<Ticket> tickets = null;
        try {
            tickets = ticketService.findSearch(texto);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Nada foi encontrado");
        }
        modelAndView.addObject("tickets", tickets);
        return modelAndView;
    }

    //CADASTRAR TICKET
    @RequestMapping(value = "/cadastrar_ticket", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView getTicket()
    {
        ModelAndView modelAndView = new ModelAndView("requerente/cadastrar_ticket");
        modelAndView.addObject("ticketObj", new Ticket());
        return modelAndView;
    }

    @PostMapping(value = "/cadastrar_ticket")
    public ModelAndView criandoTicket(@Validated Ticket ticket) {
        try {
            Ticket newticket = ticketService.save(ticket);
            return new ModelAndView("redirect:/ticket/listar_ticket");
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            ModelAndView modelAndView = new ModelAndView("requerente/cadastrar_ticket");
            modelAndView.addObject("errorMessage", errorMessage);
            modelAndView.addObject("usuarioObj", SecurityContextHolder.getContext().getAuthentication().getName());
            modelAndView.addObject("ticketObj", new Ticket());
            return modelAndView;
        }
    }

    //EXCLUIR TICKET
    @GetMapping(value = "/remover/{idTicket}")
    public ModelAndView deleteUsuario(@PathVariable("idTicket") Long id) {
        try {
            ticketService.deleteById(id);
            return new ModelAndView("redirect:/ticket/listar_ticket");
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("/requerente/listar_ticket");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            Iterable<Ticket> ticket = ticketService.listarTicket(SecurityContextHolder.getContext().getAuthentication().getName());
            modelAndView.addObject("tickets", ticket);
            return modelAndView;
        }
    }

    //VER TICKET SELECIONADO
    @GetMapping(value = {"/view_ticket/{idTicket}"})
    public ModelAndView showEditUsuario(@PathVariable("idTicket") Long idTicket) {
        try {
            ModelAndView modelAndView = new ModelAndView("/requerente/view_ticket");
            modelAndView.addObject("ticket", ticketService.findById(idTicket));
            modelAndView.addObject("comentarios", comentarioService.listarComentario(idTicket));
            return modelAndView;
        } catch (ResourceNotFoundException ex) {
            ModelAndView modelAndView = new ModelAndView("/requerente/listar_ticket");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            Iterable<Ticket> tickets = ticketService.listarTicket(SecurityContextHolder.getContext().getAuthentication().getName());
            modelAndView.addObject("tickets", tickets);
            return modelAndView;
        }
    }

    //COMENTARIO
    @PostMapping(value = {"/add_comentario/{idTicket}"})
    public ModelAndView insertComentario(@PathVariable("idTicket") Long id,
                                      @RequestParam("comentario") String comentario) {
        try {
            Comentario comentarios = new Comentario();
            comentarios.setIdTicketComentario(id);
            comentarios.setComentario(comentario);
            comentarioService.save(comentarios);
            return new ModelAndView("redirect:/ticket/view_ticket/{idTicket}");
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("/requerente/view_ticket");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            modelAndView.addObject("ticket", ticketService.findById(id));
            modelAndView.addObject("comentarios", comentarioService.listarComentario(id));
            return modelAndView;
        }
    }

    //---------------------------ATENDENTE-----------------------------------
    //LISTAR TICKET EM ANDAMENTO PARA ATENDENTE
    @RequestMapping(value = "/verificar_ticket", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView getTicketAndamentoAtendente(Model model) {
        ModelAndView modelAndView = new ModelAndView("atendente/verificar_ticket");
        Iterable<Ticket> ticket = ticketService.findTicketAndamento();
        modelAndView.addObject("tickets", ticket);
        return modelAndView;
    }

    //LISTAR TICKET SELECIONADOS PELO ATENDENTE
    @RequestMapping(value = "/ticket_atendente", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView getTicketAtendente(Model model) {
        ModelAndView modelAndView = new ModelAndView("atendente/ticket_atendente");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Iterable<Ticket> ticket = ticketService.findTicketsAtendente(email);
        modelAndView.addObject("tickets", ticket);
        return modelAndView;
    }



    //BUSCAR POR TEXTO INFORMADO ATENDENTE
    @PostMapping("/busca_ticket_atendente")
    public ModelAndView buscar_ticket_atendente(Model model, @RequestParam("texto")String texto) {
        ModelAndView modelAndView = new ModelAndView("atendente/verificar_ticket");
        Iterable<Ticket> tickets = null;
        try {
            tickets = ticketService.findSearchAtendente(texto);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Nada foi encontrado");
        }
        modelAndView.addObject("tickets", tickets);
        return modelAndView;
    }

    //BUSCAR POR TEXTO INFORMADO ATENDENTE
    @PostMapping("/busca_atendente")
    public ModelAndView buscar_atendente(Model model, @RequestParam("texto")String texto) {
        ModelAndView modelAndView = new ModelAndView("atendente/ticket_atendente");
        Iterable<Ticket> tickets = null;
        try {
            tickets = ticketService.findSearchAtendenteSelecionado(texto);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Nada foi encontrado");
        }
        modelAndView.addObject("tickets", tickets);
        return modelAndView;
    }

    //SELECIONAR TICKET ATENDENTE
    @GetMapping(value = {"/atendente_seleciona/{idTicket}"})
    public ModelAndView selecionarTicketAtendente(@PathVariable("idTicket") Long id) {
        try {
            Ticket ticket = new Ticket();
            ticket = ticketService.findById(id);
            ticket.setIdReponsavel(SecurityContextHolder.getContext().getAuthentication().getName());
            ticket.setIdTicket(id);
            ticket.setStatusTicket("Designado Atendente");
            ticketService.update(ticket);
            return new ModelAndView("redirect:/ticket/verificar_ticket");
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("requerente/view_ticket");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            modelAndView.addObject("ticket", ticketService.findById(id));
            modelAndView.addObject("comentarios", comentarioService.listarComentario(id));
            return modelAndView;
        }
    }

    //VER TICKET SELECIONADO
    @GetMapping(value = {"/view_atendente/{idTicket}"})
    public ModelAndView showTicketSelecionado(@PathVariable("idTicket") Long idTicket) {
        try {
            ModelAndView modelAndView = new ModelAndView("/atendente/view_atendente");
            modelAndView.addObject("ticket", ticketService.findById(idTicket));
            modelAndView.addObject("comentarios", comentarioService.listarComentario(idTicket));
            return modelAndView;
        } catch (ResourceNotFoundException ex) {
            ModelAndView modelAndView = new ModelAndView("/atendente/ticket_atendente");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            Iterable<Ticket> tickets = ticketService.listarTicket(SecurityContextHolder.getContext().getAuthentication().getName());
            modelAndView.addObject("tickets", tickets);
            return modelAndView;
        }
    }

    //COMENTARIO ATENDENTE
    @PostMapping(value = {"/add_comentario_atendente/{idTicket}"})
    public ModelAndView insertComentarioAtendente(@PathVariable("idTicket") Long id,
                                         @RequestParam("comentario") String comentario) {
        try {
            Comentario comentarios = new Comentario();
            comentarios.setIdTicketComentario(id);
            comentarios.setComentario(comentario);
            comentarioService.save(comentarios);
            return new ModelAndView("redirect:/ticket/view_atendente/{idTicket}");
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("/atendente/view_atendente");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            modelAndView.addObject("ticket", ticketService.findById(id));
            modelAndView.addObject("comentarios", comentarioService.listarComentario(id));
            return modelAndView;
        }
    }


}
