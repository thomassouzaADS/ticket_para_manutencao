package com.tcc.manutencao_adm_ticket.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comentario")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Long idComentario;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "email_usuario_comentario")
    private String emailUsuarioComentario;

    @Column(name = "id_ticket_comentario")
    private Long idTicketComentario;

}
