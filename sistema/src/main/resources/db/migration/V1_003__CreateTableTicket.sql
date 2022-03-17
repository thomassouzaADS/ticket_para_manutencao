create table ticket(
	id_ticket serial NOT NULL,
	titulo VARCHAR(100) NOT NULL,
	descricao VARCHAR(200) NOT NULL,
	status_ticket VARCHAR(30) NOT NULL,
	data_solicitacao DATE NOT NULL,
	email_usuario_ticket VARCHAR(50) NOT NULL,
	id_responsavel VARCHAR(50) NOT NULL,
	constraint pk_ticket primary key(id_ticket),
	constraint fk_usuario foreign key(email_usuario_ticket) references usuario(email_usuario)
);