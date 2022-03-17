package com.tcc.manutencao_adm_ticket.repositorio;

import com.tcc.manutencao_adm_ticket.model.Manutencao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ManutencaoRepository extends CrudRepository<Manutencao, Long> {

    @Transactional
    @Query(value = "select * from manutencao m inner join ticket t on m.id_ticket_manutencao = t.id_ticket\n" +
            "\tinner join usuario u on u.email_usuario = m.email_responsavel_manutencao\n" +
            "\twhere status_manutencao = 'Em andamento';", nativeQuery = true)
    Iterable<Manutencao> findManutencaoAndamento();

    @Transactional
    @Query(value = "Select * from manutencao m inner join ticket t on m.id_ticket_manutencao = t.id_ticket\n" +
            "\twhere status_manutencao = 'Manutenção em Andamento' or  status_manutencao = 'Aguardando Licitação'\n" +
            "\tand email_responsavel_manutencao = ?;", nativeQuery = true)
    Iterable<Manutencao> findManutencaoSelecionada(String email);

    @Transactional
    @Query(value = "select * from manutencao m inner join ticket t on m.id_ticket_manutencao = t.id_ticket\n" +
            "\tinner join usuario u on u.email_usuario = m.email_responsavel_manutencao\n" +
            "\twhere status_manutencao = 'Em andamento' and(\n" +
            "\tu.nome ilike CONCAT('%',:texto,'%') or \n" +
            "\tempresa ilike CONCAT('%',:texto,'%') or \n" +
            "\tcidade ilike CONCAT('%',:texto,'%') or \n" +
            "\testado ilike CONCAT('%',:texto,'%'))", nativeQuery = true)
    Iterable<Manutencao> findSearchManutencao(@Param("texto") String texto);


    @Transactional
    @Query(value = "select * from manutencao m inner join ticket t on m.id_ticket_manutencao = t.id_ticket\n" +
            "\tinner join usuario u on u.email_usuario = m.email_responsavel_manutencao\n" +
            "\twhere status_manutencao = 'Manutenção em Andamento' or status_manutencao = 'Aguardando Licitação' and(\n" +
            "\tu.nome ilike CONCAT('%',:texto,'%') or \n" +
            "\tempresa ilike CONCAT('%',:texto,'%') or \n" +
            "\tcidade ilike CONCAT('%',:texto,'%') or \n" +
            "\testado ilike CONCAT('%',:texto,'%'))", nativeQuery = true)
    Iterable<Manutencao> findSearchManutencaoSelecionada(@Param("texto") String texto);
}
