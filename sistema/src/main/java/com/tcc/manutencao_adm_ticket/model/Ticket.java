package com.tcc.manutencao_adm_ticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Long idTicket;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "status_ticket")
    private String statusTicket;


    @Column(name = "data_solicitacao")
    private Date dataSolicitacao;

    @Column(name = "id_responsavel")
    private String idReponsavel;

    @ManyToOne
    @JoinColumn(name = "email_usuario_ticket", nullable = false)
    private Usuario usuario;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Usuario> usuarioList;
}