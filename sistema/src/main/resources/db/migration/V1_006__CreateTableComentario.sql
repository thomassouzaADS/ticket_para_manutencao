create table comentario(
	id_comentario serial NOT NULL,
	comentario VARCHAR(100),
	email_usuario_comentario VARCHAR(50) NOT NULL,
	id_ticket_comentario INTEGER NOT NULL,
	constraint pk_comentario primary key(id_comentario),
	constraint fk_usuario foreign key(email_usuario_comentario) references usuario(email_usuario),
	constraint fk_ticket foreign key(id_ticket_comentario) references ticket(id_ticket)
);