--COMANDOS DDL:

--tabela que representa as DICAS no banco de dados
CREATE TABLE tip (
	id int PRIMARY KEY auto_increment,
	title varchar(200),
	description TEXT,
	user_id int
);


--tabela que representa o USUÁRIO no banco de dados
CREATE TABLE user (
	id int PRIMARY KEY auto_increment,
	name varchar(200),
	email varchar(200),
	password varchar(200)
);


--Tabela que representa os PERFIS no banco de dados
CREATE TABLE role(
	id int primary key auto_increment,
	name varchar(200)
);


--tabela associativa que mapeia o relacionamento m:n entre ROLE e USER - Com as chaves primárias de ambos
CREATE TABLE user_roles(
	user_id int,
	roles_id int
);




--COMANDOS DE INSERT:


--Os campos de senha foram inseridos já criptografados em BCrypt, para comparação com o Spring, que está criptografando no mesmo algoritmo.
--As senhas de todos os usuários pré-cadastrados abaixo são: 123.
--inserindo USUÁRIOS no banco

INSERT INTO user (name, email, password) VALUES ('Luan Henrique', 'luan@fiap.com', '$2a$12$5r/PsWAIfQMqpOkWufXvAuFK1Ve6otHkbY/9q4BTgxYYH1LiFzliG');
INSERT INTO user (name, email, password) VALUES ('Joao Carlos', 'joao@fiap.com', '$2a$12$5r/PsWAIfQMqpOkWufXvAuFK1Ve6otHkbY/9q4BTgxYYH1LiFzliG');
INSERT INTO user (name, email, password) VALUES ('Salvio Padlipskas', 'salvio@fiap.com', '$2a$12$5r/PsWAIfQMqpOkWufXvAuFK1Ve6otHkbY/9q4BTgxYYH1LiFzliG');




--inserindo DICAS no banco
INSERT INTO tip (title, description, user_id) VALUES (
	'Pesar alimentos',
	'A prática de pesar alimentos antes de comer ajuda imensamente a evitar o disperdício, tendo em vista que a pessoa irá saber a quantidade exata de comida que precisa.',
	1
);

INSERT INTO tip (title, description, user_id) VALUES (
	'Comprar/cozinhar comida por peso',
	'O hábito de pesar os alimentos também é util na hora de comprar e cozinhar a comida, para evitar grandes estoques/restos e o estrago das mesmas.', 
	2
);
	
INSERT INTO tip (title, description, user_id) VALUES (
	'Doar alimentos que não serão consumidos',
	'Muitas pessoas tem o hábito de guardar muitos alimentos em casa, o que acarreta no expiro da data de validade de alguns deles. Então para melhorar isso, podemos doar os aliemntos antes que isso ocorra.',
	3
);



--inserindo PERFIS/PAPEIS(ROLE) no banco
INSERT INTO role (name) VALUES('ROLE_ADMIN');
INSERT INTO role (name) VALUES('ROLE_USER');



--inserindo as permissões dos usuários
INSERT INTO user_roles (user_id, roles_id) VALUES (1, 1);  --Luan tem permissão de administrador
INSERT INTO user_roles (user_id, roles_id) VALUES (2, 2); --João tem permissão de usuário
INSERT INTO user_roles (user_id, roles_id) VALUES (3, 2); --Salvio tem permissão de usuário