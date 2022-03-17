--TABLE ROLE
create table role(
	nome_role VARCHAR(30) NOT NULL,
	constraint pk_cargo primary key(nome_role)
);

insert into role values ('ADMIN'), ('USER'), ('MANUTENCAO'), ('ATENDENTE');

--TRABLE USUÁRIO
create table usuario(
	nome varchar(50) NOT NULL,
	estado varchar(2) NOT NULL,
	cidade varchar(40) NOT NULL,
	empresa varchar(40) NOT NULL,
	telefone varchar(40) NOT NULL,
	email_usuario varchar(50) UNIQUE NOT NULL,
	senha varchar(50) NOT NULL,
	nome_role varchar(30) NOT NULL,
	constraint pk_usuario primary key(email_usuario),
	constraint fk_role foreign key(nome_role) references role(nome_role)
);

insert into usuario values('Admin', 'SP', 'Votuporanga', 'Padrão', '(17) 99794-4484',  'admin@admin.com', '12345', 'ADMIN');

--TABLE TICKET
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

--TABLE MANTER_USUÁRIO
create table manter_usuario(
	id_manter_usuario serial NOT NULL,
	email_usuario_alterado VARCHAR(50) NOT NULL,
	email_usuario VARCHAR(50) NOT NULL,
	alteracao VARCHAR(50),
	constraint pk_manter_usuario primary key(id_manter_usuario),
	constraint fk_usuario foreign key(email_usuario) references usuario(email_usuario)
);

--TABLE MANUTENÇÃO
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

--TABLE COMENTÁRIO
create table comentario(
	id_comentario serial NOT NULL,
	comentario VARCHAR(100),
	email_usuario_comentario VARCHAR(50) NOT NULL,
	id_ticket_comentario INTEGER NOT NULL,
	constraint pk_comentario primary key(id_comentario),
	constraint fk_usuario foreign key(email_usuario_comentario) references usuario(email_usuario),
	constraint fk_ticket foreign key(id_ticket_comentario) references ticket(id_ticket)
);
