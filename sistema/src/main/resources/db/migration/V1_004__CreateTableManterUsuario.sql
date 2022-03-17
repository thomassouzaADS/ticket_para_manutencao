create table manter_usuario(
	id_manter_usuario serial NOT NULL,
	email_usuario_alterado VARCHAR(50) NOT NULL,
	email_usuario VARCHAR(50) NOT NULL,
	alteracao VARCHAR(50),
	constraint pk_manter_usuario primary key(id_manter_usuario),
	constraint fk_usuario foreign key(email_usuario) references usuario(email_usuario)
);