DROP TABLE IF EXISTS usuarios CASCADE;
CREATE TABLE usuarios (
  codigo         SERIAL PRIMARY KEY	,
  nome       varchar(255) NOT null,
  foto      varchar(500),
  data_nascimento date not null


);