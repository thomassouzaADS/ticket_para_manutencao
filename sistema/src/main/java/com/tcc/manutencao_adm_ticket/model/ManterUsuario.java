package com.tcc.manutencao_adm_ticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "manter_usuario")
public class ManterUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_manter_usuario")
    private Long idManterUsuario;

    @Column(name = "email_usuario_alterado")
    private String emailAlterado;

    @Column(name = "email_usuario")
    private String emailUser;

    @Column(name = "alteracao")
    private String alteracao;

}
