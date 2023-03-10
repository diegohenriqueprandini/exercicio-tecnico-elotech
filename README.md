## Atividade e Objetivo

Criar uma API Rest de um cadastro de Pessoas, utilizando Spring Boot e Java

### Requisitos:

* Possuir ao menos os endpoints: GET(Buscar uma única Pessoa), GET (Busca paginada opção de filtro para retornar várias pessoas), POST, PUT, DELETE
* O cadastro de pessoa deve ter os campos: Id, Nome, CPF, Data de nascimento.
* A pessoa deve possuir uma lista de contatos (relacionamento um para muitos) com os campos: Id, Nome, Telefone e Email.
* Os dados devem ser persistidos utilizando um banco de dados relacional.
### Validações:

* Todos os campos são obrigatórios, tanto da pessoa como do contato
* A Pessoa deve possuir ao menos um contato
* O CPF deve ser um CPF válido
* A Data de nascimento não pode ser uma data futura
* Validar sintaxe do email do contato

### Requisitos técnicos:

* Deverão ser criados testes unitários
* Publicar o código em repositório público

### É opcional e será um diferencial:

* Implementar o front-end para consumir a API.Desejável que seja em ReactJS ou Angular
* Publicar a aplicação na internet utilizando algum provedor, para que possa ser acessado sem necessidade de rodar o projeto local

### Itens Pendendes
* Testar api de contatos
* Criar VOs de contatos
* Formatação do telefone
* frontend (parcial)
* deploy (nao deu tempo)
* bug referente a consistencia dos contatos da pessoa, ao editar pelo frontend

### Obrigatório para subir local
* Java 17
* Docker
* Docker Compose

### Obrigatório para subir em produção 
* Java 17
* Postgres server

### Required java VM Args
* -Dspring.profiles.active=prod -> (dev, prod)
* -Dspring.datasource.url=jdbc:postgresql://#####:5432/exercicio-tecnico-elotech -> postgres host
* -Dspring.datasource.username=#####
* -Dspring.datasource.password=#####

### Subir local para desenvolvimento
* ./start.sh

### Stopt local para desenvolvimento
* ./stop.sh