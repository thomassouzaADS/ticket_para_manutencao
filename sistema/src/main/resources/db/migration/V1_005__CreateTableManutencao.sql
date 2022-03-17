create table manutencao(
	id_manutencao serial NOT NULL,
    status_manutencao VARCHAR(30),
    descricao VARCHAR(100),
    materiais_utilizados VARCHAR(100),
    data_execucao DATE,
    email_responsavel_manutencao VARCHAR(50),
    id_ticket_manutencao INTEGER NOT NULL,
    constraint id_manutencao primary key(id_manutencao),
    constraint fk_ticket foreign key(id_ticket_manutencao) references ticket(id_ticket),
    constraint fk_usuario foreign key(email_responsavel_manutencao) references usuario(email_usuario)
);