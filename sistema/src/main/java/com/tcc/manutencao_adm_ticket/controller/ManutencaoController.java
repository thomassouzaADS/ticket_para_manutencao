package com.tcc.manutencao_adm_ticket.controller;

import com.tcc.manutencao_adm_ticket.model.*;
import com.tcc.manutencao_adm_ticket.service.ComentarioService;
import com.tcc.manutencao_adm_ticket.service.ManutencaoService;
import com.tcc.manutencao_adm_ticket.service.TicketService;
import com.tcc.manutencao_adm_ticket.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;

@RestController
@RequestMapping("/manutencao")
@Controller
public class ManutencaoController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private ManutencaoService manutencaoService;

    //DESIGNAR TICKET PARA MANUTENÇÃO
    @GetMapping(value = {"/designar_manutencao/{idTicket}"})
    public ModelAndView designarManutencao(@PathVariable("idTicket") Long id) {
        try {
            Manutencao manutencao = new Manutencao();
            Ticket ticket = new Ticket();
            ticket = ticketService.findById(id);
            ticket.setIdTicket(id);
            ticket.setStatusTicket("Designado Manutenção");
            manutencao.setUsuario(usuarioService.findByEmailManutencao(SecurityContextHolder.getContext().getAuthentication().getName()));
            manutencao.setTicket(ticketService.findByIdManutencao(id));
            manutencaoService.save(manutencao);
            ticketService.update(ticket);
            return new ModelAndView("redirect:/ticket/ticket_atendente");
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("atendente/view_atendente");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            modelAndView.addObject("ticket", ticketService.findById(id));
            modelAndView.addObject("comentarios", comentarioService.listarComentario(id));
            return modelAndView;
        }
    }

    //LISTAR TICKET EM ANDAMENTO PARA O FUNCIONARIO DA MANUTENCAO
    @RequestMapping(value = "/verificar_manutencao", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView getManutencaoAndamento(Model model) {
        ModelAndView modelAndView = new ModelAndView("manutencao/verificar_manutencao");
        Iterable<Manutencao> manutencao = manutencaoService.findManutencaoAndamento();
        modelAndView.addObject("manutencoes", manutencao);
        return modelAndView;
    }

    //LISTAR TICKET SELECIONADOS PELO FUNCIONARIO DA MANUTENCAO
    @RequestMapping(value = "/ticket_manutencao", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView getTicketManutencao(Model model) {
        ModelAndView modelAndView = new ModelAndView("manutencao/ticket_manutencao");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Iterable<Manutencao> manutencao = manutencaoService.findTicketsManutencao(email);
        modelAndView.addObject("manutencoes", manutencao);
        return modelAndView;
    }



    //BUSCAR POR TEXTO INFORMADO ATENDENTE
    @PostMapping("/busca_ticket_manutencao")
    public ModelAndView buscarTicketManutencao(Model model, @RequestParam("texto")String texto) {
        ModelAndView modelAndView = new ModelAndView("manutencao/verificar_manutencao");
        Iterable<Manutencao> manutencoes = null;
        try {
            manutencoes = manutencaoService.findSearchManutencao(texto);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Nada foi encontrado");
        }
        modelAndView.addObject("manutencoes", manutencoes);
        return modelAndView;
    }

    //BUSCAR POR TEXTO INFORMADO FUNCIONARIO MANUTENCAO
    @PostMapping("/busca_manutencao")
    public ModelAndView buscarManutencao(Model model, @RequestParam("texto")String texto) {
        ModelAndView modelAndView = new ModelAndView("manutencao/ticket_manutencao");
        Iterable<Manutencao> manutencaos = null;
        try {
            manutencaos = manutencaoService.findSearchManutencaoSelecionada(texto);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Nada foi encontrado");
        }
        modelAndView.addObject("manutencoes", manutencaos);
        return modelAndView;
    }

    //VER TICKET
    @GetMapping(value = {"/view_manutencao/{idManutencao}"})
    public ModelAndView showTicket(@PathVariable("idManutencao") Long idManutencao) {
        try {
            ModelAndView modelAndView = new ModelAndView("/manutencao/view_manutencao");
            modelAndView.addObject("manutencao", manutencaoService.findByIdManutencao(idManutencao));
            modelAndView.addObject("comentarios", comentarioService.findComentariosManutencao(idManutencao));
            return modelAndView;
        } catch (ResourceNotFoundException ex) {
            ModelAndView modelAndView = new ModelAndView("/manutencao/verificar_manutencao");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            Iterable<Manutencao> manutencaos = manutencaoService.findManutencaoAndamento();
            modelAndView.addObject("manutencoes", manutencaos);
            return modelAndView;
        }
    }

    //VER TICKET SELECIONADO
    @GetMapping(value = {"/view_manutencao_selecionado/{idManutencao}"})
    public ModelAndView showTicketSelecionado(@PathVariable("idManutencao") Long idManutencao) {
        try {
            ModelAndView modelAndView = new ModelAndView("/manutencao/view_manutencao_selecionado");
            modelAndView.addObject("manutencao", manutencaoService.findByIdManutencao(idManutencao));
            modelAndView.addObject("comentarios", comentarioService.findComentariosManutencao(idManutencao));
            return modelAndView;
        } catch (ResourceNotFoundException ex) {
            ModelAndView modelAndView = new ModelAndView("/manutencao/ticket_manutencao");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            Iterable<Manutencao> manutencaos = manutencaoService.findManutencaoAndamento();
            modelAndView.addObject("manutencoes", manutencaos);
            return modelAndView;
        }
    }

    //COMENTARIO MANUTENCAO
    @PostMapping(value = {"/add_comentario_manutencao/{idTicket}"})
    public ModelAndView insertComentarioAtendente(@PathVariable("idTicket") Long id,
                                                  @RequestParam("comentario") String comentario,
                                                  @RequestParam("idManutencao") Long idManutencao){
        try {
            Long idManutencaoParam = idManutencao;
            Comentario comentarios = new Comentario();
            comentarios.setIdTicketComentario(id);
            comentarios.setComentario(comentario);
            comentarioService.save(comentarios);
            return new ModelAndView("redirect:/manutencao/view_manutencao/"+idManutencaoParam);
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("/manutencao/view_manutencao");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            modelAndView.addObject("manutencao", manutencaoService.findById(idManutencao));
            modelAndView.addObject("comentarios", comentarioService.findComentariosManutencao(idManutencao));
            return modelAndView;
        }
    }

    //COMENTARIO MANUTENCAO SELECIONADO
    @PostMapping(value = {"/add_comentario/{idTicket}"})
    public ModelAndView insertComentarioTicketSelecionado(@PathVariable("idTicket") Long id,
                                                  @RequestParam("comentario") String comentario,
                                                  @RequestParam("idManutencao") Long idManutencao){
        try {
            Long idManutencaoParam = idManutencao;
            Comentario comentarios = new Comentario();
            comentarios.setIdTicketComentario(id);
            comentarios.setComentario(comentario);
            comentarioService.save(comentarios);
            return new ModelAndView("redirect:/manutencao/view_manutencao_selecionado/"+idManutencaoParam);
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("/manutencao/view_manutencao_selecionado");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            modelAndView.addObject("manutencao", manutencaoService.findById(idManutencao));
            modelAndView.addObject("comentarios", comentarioService.findComentariosManutencao(idManutencao));
            return modelAndView;
        }
    }

    //SELECIONAR TICKET MANUTENCAO
    @GetMapping(value = {"/selecionar_manutencao/{idManutencao}"})
    public ModelAndView selecionarTicketAtendente(@PathVariable("idManutencao") Long id) {
        try {
            Manutencao manutencao = new Manutencao();
            manutencao = manutencaoService.findByIdManutencao(id);
            manutencao.setUsuario(usuarioService.findByEmailManutencao(SecurityContextHolder.getContext().getAuthentication().getName()));
            manutencao.setIdManutencao(id);
            manutencao.setStatusManutencao("Manutenção em Andamento");
            manutencaoService.update(manutencao);
            return new ModelAndView("redirect:/manutencao/verificar_manutencao");
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("manutencao/view_manutencao");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            modelAndView.addObject("manutencao", manutencaoService.findById(id));
            modelAndView.addObject("comentarios", comentarioService.findComentariosManutencao(id));
            return modelAndView;
        }
    }

    //LICITAÇÃO TICKET SELECIONADO MANUTENCAO
    @GetMapping(value = {"/licitacao/{idManutencao}"})
    public ModelAndView licitacao(@PathVariable("idManutencao") Long id){
        try {
            Manutencao manutencao = new Manutencao();
            manutencao = manutencaoService.findByIdManutencao(id);
            manutencao.setUsuario(usuarioService.findByEmailManutencao(SecurityContextHolder.getContext().getAuthentication().getName()));
            manutencao.setIdManutencao(id);
            manutencao.setStatusManutencao("Aguardando Licitação");

            Ticket ticket = new Ticket();
            Long ticketId = ticketService.buscaTicketManutencao(id);
            ticket = ticketService.findByIdManutencao(ticketId);
            ticket.setStatusTicket("Aguardando Licitação");

            manutencaoService.update(manutencao);
            ticketService.update(ticket);
            return new ModelAndView("redirect:/manutencao/ticket_manutencao");
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("manutencao/view_manutencao_selecionado");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            modelAndView.addObject("manutencao", manutencaoService.findById(id));
            modelAndView.addObject("comentarios", comentarioService.findComentariosManutencao(id));
            return modelAndView;
        }
    }

    //SELECIONAR TICKET MANUTENCAO
    @GetMapping(value = {"/finalizar/{idManutencao}"})
    public ModelAndView finalizar(@PathVariable("idManutencao") Long idManutencao) {
        try {
            ModelAndView modelAndView = new ModelAndView("/manutencao/finalizar_ticket");
            modelAndView.addObject("manutencao", manutencaoService.findByIdManutencao(idManutencao));
            return modelAndView;
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("manutencao/view_manutencao");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            modelAndView.addObject("manutencao", manutencaoService.findById(idManutencao));
            modelAndView.addObject("comentarios", comentarioService.findComentariosManutencao(idManutencao));
            return modelAndView;
        }
    }

    //SELECIONAR TICKET MANUTENCAO
    @PostMapping(value = {"/finalizar/{idManutencao}"})
    public ModelAndView finalizarTicket(@PathVariable("idManutencao") Long idManutencao,
                                        @RequestParam("descricao") String descricao,
                                        @RequestParam("materiaisUtilizados") String materiaisUtilizados) {
        try {
            Manutencao manutencao = new Manutencao();
            manutencao = manutencaoService.findByIdManutencao(idManutencao);
            manutencao.setUsuario(usuarioService.findByEmailManutencao(SecurityContextHolder.getContext().getAuthentication().getName()));
            manutencao.setIdManutencao(idManutencao);
            manutencao.setStatusManutencao("Finalizado");
            Date data = new Date(System.currentTimeMillis());
            manutencao.setDataExecucao(data);
            manutencao.setDescricao(descricao);
            manutencao.setMateriaisUtilizados(materiaisUtilizados);

            Ticket ticket = new Ticket();
            Long ticketId = ticketService.buscaTicketManutencao(idManutencao);
            ticket = ticketService.findByIdManutencao(ticketId);
            ticket.setStatusTicket("Finalizado");

            manutencaoService.update(manutencao);
            ticketService.update(ticket);

            manutencaoService.update(manutencao);
            ticketService.update(ticket);
            return new ModelAndView("redirect:/manutencao/ticket_manutencao");
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("manutencao/finalizar");
            String errorMessage = ex.getMessage();
            modelAndView.addObject("errorMessage", errorMessage);
            modelAndView.addObject("manutencao", manutencaoService.findById(idManutencao));
            modelAndView.addObject("comentarios", comentarioService.findComentariosManutencao(idManutencao));
            return modelAndView;
        }
    }

}
