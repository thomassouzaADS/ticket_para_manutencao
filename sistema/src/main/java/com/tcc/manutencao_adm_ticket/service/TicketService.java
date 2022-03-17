package com.tcc.manutencao_adm_ticket.service;

import com.tcc.manutencao_adm_ticket.exception.BadResourceException;
import com.tcc.manutencao_adm_ticket.exception.ResourceAlreadyExistsException;
import com.tcc.manutencao_adm_ticket.model.Ticket;
import com.tcc.manutencao_adm_ticket.model.Usuario;
import com.tcc.manutencao_adm_ticket.repositorio.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.sql.Date;

@Service
public class TicketService {


    @Autowired
    TicketRepository ticketRepository;;

    @Autowired
    private UsuarioService usuarioSerice;

    private boolean existsById(Long id) {
        return ticketRepository.existsById(id);
    }

    public Iterable<Ticket> listarTicket(String email) throws ResourceNotFoundException {
        Iterable<Ticket> ticket = ticketRepository.findByEmail(email);
        return ticket;
    }

    //BUSCA DE TICKETS ATENDENTE
    public Iterable<Ticket> findTicketsAtendente(String email) throws ResourceNotFoundException {
        Iterable<Ticket> ticket = ticketRepository.findTicketAtendente(email);
        return ticket;
    }

    public Iterable<Ticket> findSearchAtendente(String texto) throws ResourceNotFoundException {
        Iterable<Ticket> ticket = ticketRepository.findSearchAtendente(texto);
        return ticket;
    }

    public Iterable<Ticket> findSearchAtendenteSelecionado(String texto) throws ResourceNotFoundException {
        Iterable<Ticket> ticket = ticketRepository.findSearchAtendenteSelecionado(texto);
        return ticket;
    }

    //BUSCANDO POR ID
    public Ticket findById(Long id) throws org.springframework.data.rest.webmvc.ResourceNotFoundException {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket==null) {
            throw new org.springframework.data.rest.webmvc.ResourceNotFoundException("Não foi possivel encontrar o seguinte id: " + id);
        }
        else return ticket;
    }

    //BUSCANDO POR ID
    public Ticket findByIdManutencao(Long id) throws org.springframework.data.rest.webmvc.ResourceNotFoundException {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket==null) {
            throw new org.springframework.data.rest.webmvc.ResourceNotFoundException("Não foi possivel encontrar o seguinte id: " + id);
        }
        else return ticket;
    }

    //BUSCANDO ID COM MANUTENCAO
    public Long buscaTicketManutencao(Long id) throws org.springframework.data.rest.webmvc.ResourceNotFoundException {
        Long ticket = ticketRepository.buscaTicketManutencao(id);
        if (ticket==null) {
            throw new org.springframework.data.rest.webmvc.ResourceNotFoundException("Não foi possivel encontrar o seguinte id: " + id);
        }
        else return ticket;
    }

    //CAMPO BUSCA
    public Iterable<Ticket> findSearch(String texto){
        Iterable<Ticket> tickets = ticketRepository.findSearch(texto,SecurityContextHolder.getContext().getAuthentication().getName());
        if (tickets==null) {
            throw new ResourceNotFoundException("Não foi possivel encontrar a seguinte informação: " + texto);
        }
            else return tickets;

    }

    public Iterable<Ticket> findTicketAndamento() {
        Iterable<Ticket> tickets = ticketRepository.findTicketAndamento();
        return tickets;
    }


    public Ticket save(Ticket ticket) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(String.valueOf(ticket.getIdTicket()))) {
            if (ticket.getIdTicket() != null && existsById(ticket.getIdTicket())) {
                throw new ResourceAlreadyExistsException("Ticket com o id: " + ticket.getIdTicket() +
                        " ja existe");
            }
            Date data = new Date(System.currentTimeMillis());
            ticket.setDataSolicitacao(data);
            ticket.setUsuario(usuarioSerice.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
            ticket.setStatusTicket("Em andamento");
            ticket.setIdReponsavel("FALSE");
            return ticketRepository.save(ticket);
        }
        else {
            BadResourceException exc = new BadResourceException("Falha ao salvar o ticket");
            exc.addErrorMessage("Ticket vazio ou nulo");
            throw exc;
        }
    }

    public void update(Ticket ticket)
            throws BadResourceException, org.springframework.data.rest.webmvc.ResourceNotFoundException {
        if (!StringUtils.isEmpty(ticket.getIdTicket().toString())) {
            if (!existsById(ticket.getIdTicket())) {
                throw new org.springframework.data.rest.webmvc.ResourceNotFoundException("Não foi possivel localizar este Ticket: " + ticket.getIdTicket());
            }
            ticketRepository.save(ticket);
        }
        else {
            BadResourceException exc = new BadResourceException("Falha ao editar o ticket");
            exc.addErrorMessage("Ticket vazio ou nulo");
            throw exc;
        }
    }


    public void deleteById(Long id) throws BadResourceException, org.springframework.data.rest.webmvc.ResourceNotFoundException {
        Ticket ticket = findById(id);
        if (!existsById(id)) {
                throw new ResourceNotFoundException("Não foi possivel encontrar um ticket com o id: " + id);
            }else{
                if (ticket.getStatusTicket().equals("Finalizado"))
                ticketRepository.deleteById(id);
                else
                    throw new ResourceNotFoundException("Não foi possivel deletar, pois o ticket não foi finalizado");
            }
        }
}
