package com.tcc.manutencao_adm_ticket.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "manutencao")
public class Manutencao {

    @Id
    @Column(name = "id_manutencao")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idManutencao;

    @Column(name = "status_manutencao")
    private String statusManutencao;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "materiais_utilizados")
    private String materiaisUtilizados;

    @Column(name = "data_execucao")
    private Date dataExecucao;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email_responsavel_manutencao")
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_ticket_manutencao")
    private Ticket ticket;
}
