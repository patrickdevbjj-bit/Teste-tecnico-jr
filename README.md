# Desafio Java Jr - API de Gerenciamento de Livros

## Autor
- Nome: `Patrick Rodrigues de Freitas`
- Contato: `patrickdev6@hotmail.com`

## Sobre o projeto
Este projeto implementa uma API REST simples para gerenciamento de livros usando Spring Boot.

A aplicacao permite:
- cadastrar livros
- listar livros cadastrados
- atualizar um livro pelo `id`
- excluir um livro pelo `id`

Os dados sao persistidos em banco H2 em memoria, atendendo ao requisito de simplicidade do desafio.

## Tecnologias utilizadas
- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- H2 Database
- Bean Validation
- Lombok
- JUnit 5
- Mockito

## Decisoes tecnicas
- Spring Data JPA foi utilizado para simplificar o acesso a dados e reduzir codigo repetitivo de persistencia.
- H2 foi escolhido por ser um banco em memoria, facil de configurar e ideal para testes tecnicos.
- DTOs foram usados para separar o contrato da API da entidade persistida.
- A camada de service concentra a regra da aplicacao e evita controller com responsabilidade excessiva.
- O tratamento global de excecoes padroniza respostas de erro da API.
- Lombok foi utilizado para reduzir boilerplate em classes simples, como entidade e DTOs.

## Estrutura do projeto
O codigo fonte da aplicacao esta dentro da pasta:

`desafio-java-jr/`

Principais pacotes:
- `controller`: endpoints REST
- `service`: regras de negocio
- `repository`: acesso a dados
- `entity`: entidade JPA
- `dto`: objetos de entrada e saida da API
- `exception`: tratamento de erros
- `test`: testes automatizados

## Entidade principal
### Livro
Campos da entidade:
- `id` (`Long`): identificador unico
- `titulo` (`String`): titulo do livro
- `autor` (`String`): autor do livro
- `anoPublicacao` (`Integer`): ano de publicacao

## Como executar o projeto
Entre na pasta interna do projeto:

```powershell
cd desafio-java-jr
```

Execute a aplicacao com Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

Por padrao, a API sera iniciada em:

`http://localhost:8080`

## Como executar os testes
Na pasta `desafio-java-jr`, rode:

```powershell
.\mvnw.cmd test
```

## Banco H2
O projeto usa H2 em memoria.

Configuracoes principais:
- URL JDBC: `jdbc:h2:mem:livrosdb`
- Usuario: `sa`
- Senha: vazia

Console do H2:
- URL: `http://localhost:8080/h2-console`

Para acessar o console:
- JDBC URL: `jdbc:h2:mem:livrosdb`
- User Name: `sa`
- Password: deixar em branco

## Endpoints da API
Base URL:

`http://localhost:8080/livros`

### 1. Cadastrar livro
**POST** `/livros`

Request:

```json
{
  "titulo": "Revolucao dos Bichos",
  "autor": "George Orwell",
  "anoPublicacao": 1945
}
```

Resposta esperada:
- Status `201 Created`

Exemplo de response:

```json
{
  "id": 1,
  "titulo": "Revolucao dos Bichos",
  "autor": "George Orwell",
  "anoPublicacao": 1945
}
```

### 2. Listar livros
**GET** `/livros`

Resposta esperada:
- Status `200 OK`

Exemplo de response:

```json
[
  {
    "id": 1,
    "titulo": "Revolucao dos Bichos",
    "autor": "George Orwell",
    "anoPublicacao": 1945
  }
]
```

### 3. Atualizar livro
**PUT** `/livros/{id}`

Exemplo:

`PUT /livros/1`

Request:

```json
{
  "titulo": "Revolucao dos Bichos - Edicao Atualizada",
  "autor": "George Orwell",
  "anoPublicacao": 1946
}
```

Resposta esperada:
- Status `200 OK`

Exemplo de response:

```json
{
  "id": 1,
  "titulo": "Revolucao dos Bichos - Edicao Atualizada",
  "autor": "George Orwell",
  "anoPublicacao": 1946
}
```

### 4. Excluir livro
**DELETE** `/livros/{id}`

Exemplo:

`DELETE /livros/1`

Resposta esperada:
- Status `204 No Content`

## Validacoes implementadas
No request de criacao e atualizacao:
- `titulo` e obrigatorio
- `autor` e obrigatorio
- `anoPublicacao` e obrigatorio

## Tratamento de erros
### Livro nao encontrado
Quando um livro nao existe para atualizacao ou exclusao:
- Status `404 Not Found`

Exemplo de response:

```json
{
  "erro": "Livro nao encontrado com o id: 999"
}
```

### Erro de validacao
Quando o corpo da requisicao e invalido:
- Status `400 Bad Request`

Exemplo de response:

```json
{
  "titulo": "O titulo e obrigatorio.",
  "autor": "O autor e obrigatorio.",
  "anoPublicacao": "O ano de publicacao e obrigatorio."
}
```

## Testes implementados
Foram adicionados testes automatizados para validar o comportamento da aplicacao.

### Testes de controller
Cobrem:
- cadastro de livro
- listagem de livros
- atualizacao de livro
- exclusao de livro
- erro `404` para livro inexistente
- erro `400` para request invalido

### Testes de service
Cobrem:
- criacao de livro
- listagem de livros
- atualizacao com sucesso
- atualizacao de livro inexistente
- exclusao com sucesso
- exclusao de livro inexistente

## Observacoes finais
- A API foi testada manualmente com Postman.
- Os testes automatizados estao passando.
- O projeto foi estruturado em camadas para manter clareza, separacao de responsabilidades e facilidade de manutencao.
