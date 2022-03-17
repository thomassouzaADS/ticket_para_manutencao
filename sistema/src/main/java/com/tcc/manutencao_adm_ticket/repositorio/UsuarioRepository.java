package com.tcc.manutencao_adm_ticket.repositorio;

import com.tcc.manutencao_adm_ticket.model.Usuario;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, String> {
    @Transactional
    @Query(value = "select * from usuario where email_usuario = ?", nativeQuery = true)
    Usuario findByEmail(String email);

    @Transactional
    @Query(value = "select email_usuario from usuario where email_usuario = ?", nativeQuery = true)
    Usuario findByEmailManutencao(String email);

    @Transactional
    @Query(value = "select senha from usuario where email_usuario = ?", nativeQuery = true)
    String findByPass(String email);

    @Transactional
    @Query(value = "select * from usuario where nome ilike CONCAT('%',:texto,'%')\n" +
            "\tor estado ilike CONCAT('%',:texto,'%')\n" +
            "\tor cidade ilike CONCAT('%',:texto,'%')\n" +
            "\tor empresa ilike CONCAT('%',:texto,'%')\n" +
            "\tor email_usuario ilike CONCAT('%',:texto,'%')\n" +
            "\tor nome_role ilike CONCAT('%',:texto,'%')", nativeQuery = true)
    Iterable<Usuario> findSearch(@Param("texto") String texto);

    @Transactional
    @Modifying
    @Query(value = "delete from usuario where email_usuario = ? and email_usuario != 'admin@admin.com'", nativeQuery = true)
    void deleteByEmail(String email);
}