package com.tcc.manutencao_adm_ticket.service;

import com.tcc.manutencao_adm_ticket.exception.BadResourceException;
import com.tcc.manutencao_adm_ticket.exception.ResourceAlreadyExistsException;
import com.tcc.manutencao_adm_ticket.model.Manutencao;
import com.tcc.manutencao_adm_ticket.repositorio.ManutencaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.sql.Date;

@Service
public class ManutencaoService {

    @Autowired
    ManutencaoRepository manutencaoRepository;

    private boolean existsById(Long id) {
        return manutencaoRepository.existsById(id);
    }

    public Manutencao findById(Long id) throws org.springframework.data.rest.webmvc.ResourceNotFoundException {
        Manutencao manutencao = manutencaoRepository.findById(id).orElse(null);
        if (manutencao==null) {
            throw new org.springframework.data.rest.webmvc.ResourceNotFoundException("Não foi possivel encontrar o seguinte id: " + id);
        }
        else return manutencao;
    }

    public Manutencao findByIdManutencao(Long id) throws org.springframework.data.rest.webmvc.ResourceNotFoundException {
        Manutencao manutencao = manutencaoRepository.findById(id).orElse(null);
        if (manutencao==null) {
            throw new org.springframework.data.rest.webmvc.ResourceNotFoundException("Não foi possivel encontrar o seguinte id: " + id);
        }
        else return manutencao;
    }

    public Iterable<Manutencao> findManutencaoAndamento() {
        Iterable<Manutencao> manutencaos = manutencaoRepository.findManutencaoAndamento();
        return manutencaos;
    }

    public Iterable<Manutencao> findSearchManutencao(String texto) throws ResourceNotFoundException {
        Iterable<Manutencao> manutencoes = manutencaoRepository.findSearchManutencao(texto);
        return manutencoes;
    }

    public Iterable<Manutencao> findSearchManutencaoSelecionada(String texto) throws ResourceNotFoundException {
        Iterable<Manutencao> manutencoes = manutencaoRepository.findSearchManutencaoSelecionada(texto);
        return manutencoes;
    }

    //BUSCA DE TICKETS QUE O FUNCIONARIO SELECIONOU
    public Iterable<Manutencao> findTicketsManutencao(String email) throws ResourceNotFoundException {
        Iterable<Manutencao> manutencoes = manutencaoRepository.findManutencaoSelecionada(email);
        return manutencoes;
    }


    public Manutencao save(Manutencao manutencao) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(String.valueOf(manutencao.getIdManutencao()))) {
            if (manutencao.getIdManutencao() != null && existsById(manutencao.getIdManutencao())) {
                throw new ResourceAlreadyExistsException("Manutenção com o id: " + manutencao.getIdManutencao() +
                        " ja existe");
            }
            Date data = new Date(System.currentTimeMillis());
            manutencao.setStatusManutencao("Em andamento");
            return manutencaoRepository.save(manutencao);
        }
        else {
            BadResourceException exc = new BadResourceException("Falha ao salvar o manutenção");
            exc.addErrorMessage("Manutenção vazio ou nulo");
            throw exc;
        }
    }

    public void update(Manutencao manutencao)
            throws BadResourceException, org.springframework.data.rest.webmvc.ResourceNotFoundException {
        if (!StringUtils.isEmpty(manutencao.getIdManutencao().toString())) {
            if (!existsById(manutencao.getIdManutencao())) {
                throw new org.springframework.data.rest.webmvc.ResourceNotFoundException("Não foi possivel localizar esta Manutencao: "
                        + manutencao.getIdManutencao());
            }
            manutencaoRepository.save(manutencao);
        }
        else {
            BadResourceException exc = new BadResourceException("Falha ao editar o manutencao");
            exc.addErrorMessage("Manutencao vazio ou nulo");
            throw exc;
        }
    }

}
