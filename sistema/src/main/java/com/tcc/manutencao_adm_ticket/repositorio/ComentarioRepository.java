package com.tcc.manutencao_adm_ticket.repositorio;

import com.tcc.manutencao_adm_ticket.model.Comentario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ComentarioRepository extends CrudRepository<Comentario, Long> {

    @Transactional
    @Query(value = "select * from comentario c\n" +
            "    inner join ticket t\n" +
            "    on c.id_ticket_comentario = t.id_ticket\n" +
            "    where c.id_ticket_comentario = ?", nativeQuery = true)
    Iterable<Comentario> findComentarios(Long id);

    @Transactional
    @Query(value = "select * from manutencao m\n" +
            "\tinner join ticket t\n" +
            "\ton m.id_ticket_manutencao = t.id_ticket\n" +
            "\tinner join comentario c\n" +
            "\ton t.id_ticket = c.id_ticket_comentario\n" +
            "\twhere m.id_manutencao =  ?", nativeQuery = true)
    Iterable<Comentario> findComentariosManutencao(Long id);


}
