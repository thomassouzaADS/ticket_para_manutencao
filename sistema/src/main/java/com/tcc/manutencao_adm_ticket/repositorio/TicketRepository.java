package com.tcc.manutencao_adm_ticket.repositorio;

import com.tcc.manutencao_adm_ticket.model.Ticket;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TicketRepository  extends CrudRepository<Ticket, Long> {
    @Transactional
    @Query(value = "select * from ticket t\n" +
            "inner join usuario u\n" +
            "on t.email_usuario_ticket = u.email_usuario\n" +
            "where u.email_usuario = ?", nativeQuery = true)
    Iterable<Ticket> findByEmail(String email);

    @Transactional
    @Query(value = "select * from ticket where email_usuario_ticket ilike CONCAT('%',:email,'%') and\n" +
            "\tstatus_ticket ilike CONCAT('%',:texto,'%')", nativeQuery = true)
    Iterable<Ticket> findSearch(@Param("texto") String texto, @Param("email") String email);

    @Transactional
    @Query(value = "select t.id_ticket from ticket t\n" +
            "inner join manutencao m\n" +
            "on t.id_ticket = m.id_ticket_manutencao\n" +
            "where m.id_manutencao = ?", nativeQuery = true)
    Long buscaTicketManutencao(Long idManutencao);

    //QUERY ATENDENTE
    @Transactional
    @Query(value = "select * from ticket t inner join usuario u on t.email_usuario_ticket = u.email_usuario\n" +
            "\twhere id_responsavel = 'FALSE' and status_ticket = 'Em andamento' order by estado", nativeQuery = true)
    Iterable<Ticket> findTicketAndamento();

    @Transactional
    @Query(value = "select * from ticket t inner join usuario u on t.email_usuario_ticket = u.email_usuario\n" +
            "\twhere id_responsavel = ? and status_ticket = 'Designado Atendente' order by estado", nativeQuery = true)
    Iterable<Ticket> findTicketAtendente(String email);

    @Transactional
    @Query(value = "select * from ticket t\n" +
            "\tinner join usuario u on t.email_usuario_ticket = u.email_usuario\n" +
            "\twhere t.status_ticket ilike '%Em andamento%' and(\n" +
            "\tu.nome ilike CONCAT('%',:texto,'%') or \n" +
            "\tempresa ilike CONCAT('%',:texto,'%') or \n" +
            "\tcidade ilike CONCAT('%',:texto,'%') or \n" +
            "\testado ilike CONCAT('%',:texto,'%'))", nativeQuery = true)
    Iterable<Ticket> findSearchAtendente(@Param("texto") String texto);

    @Transactional
    @Query(value = "select * from ticket t\n" +
            "\tinner join usuario u on t.email_usuario_ticket = u.email_usuario\n" +
            "\tinner join usuario a on t.id_responsavel = a.email_usuario\n" +
            "\twhere t.status_ticket ilike '%Designado Atendente%' and(\n" +
            "\tu.nome ilike CONCAT('%',:texto,'%') or \n" +
            "\tempresa ilike CONCAT('%',:texto,'%') or \n" +
            "\tcidade ilike CONCAT('%',:texto,'%') or \n" +
            "\testado ilike CONCAT('%',:texto,'%'))", nativeQuery = true)
    Iterable<Ticket> findSearchAtendenteSelecionado(@Param("texto") String texto);

//    //QUERY MANUTENCAO
//    @Transactional
//    @Query(value = "select * from ticket where id_responsavel = 'FALSE' and status_ticket = 'Designado Manutenção'", nativeQuery = true)
//    Iterable<Ticket> findTicketManutencao();
//
//    @Transactional
//    @Query(value = "select * from ticket where id_responsavel = ? and status_ticket = 'Manutenção em Andamento'\n" +
//            "\tand status_ticket = 'Aguardando Licitação", nativeQuery = true)
//    Iterable<Ticket> findTicketFuncManutencao(String email);


}
