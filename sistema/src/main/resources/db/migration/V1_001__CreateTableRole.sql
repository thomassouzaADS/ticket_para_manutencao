create table role(
	nome_role VARCHAR(30) NOT NULL,
	constraint pk_cargo primary key(nome_role)
);

insert into role values ('ADMIN'), ('USER'), ('MANUTENCAO'), ('ATENDENTE');