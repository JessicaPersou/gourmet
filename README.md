# Gourmet - Sistema de Reservas para Restaurantes

Este projeto é uma aplicação de gerenciamento de restaurantes, reservas e avaliações. Desenvolvido com Spring Boot, utiliza PostgreSQL como banco de dados e é containerizado com Docker para facilitar a implantação.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4.3
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- Maven

## Requisitos

Para executar este projeto, você precisa ter instalado:

- JDK 21
- Docker e Docker Compose
- Maven (opcional, pois o projeto utiliza Maven Wrapper)

## Estrutura do Projeto

O projeto segue uma arquitetura limpa com separação clara de responsabilidades:

- **Domínio**: Contém as entidades de negócio, interfaces de repositório e regras de domínio.
- **Aplicação**: Contém os casos de uso que implementam a lógica de negócio.
- **Adaptadores**: Implementam interfaces de repositório e controladores REST.
- **Infraestrutura**: Configurações de banco de dados, Spring Boot, etc.

## Como Executar

### Usando Docker Compose (Recomendado)

1. Clone o repositório:
   ```bash
   git clone https://github.com/JessicaPersou/gourmet.git
   cd gourmet
   ```

2. Configure a variável de ambiente para o Docker Compose:
   ```bash
   export DOCKER_USERNAME=seu_usuario_docker
   ```
   Ou substitua `${DOCKER_USERNAME}` no arquivo `docker-compose.yaml` pelo seu nome de usuário Docker.

3. Execute com Docker Compose:
   ```bash
   docker-compose up -d
   ```

   Isso irá:
    - Iniciar um contêiner PostgreSQL na porta 5432
    - Construir a aplicação Spring Boot e iniciá-la na porta 8080

4. Acesse a aplicação em http://localhost:8080

### Executando Localmente

1. Clone o repositório:
   ```bash
   git clone [URL_DO_REPOSITORIO]
   cd gourmet
   ```

2. Configure um banco de dados PostgreSQL:
    - Crie um banco de dados chamado `gourmet_db`
    - Usuário: `postgres`
    - Senha: `postgres`
    - Ou ajuste as configurações em `src/main/resources/application.properties`

3. Execute a aplicação usando o Maven Wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Acesse a aplicação em http://localhost:8080

## Documentação da API

A API está documentada usando Swagger UI. Após iniciar a aplicação, acesse:

- http://localhost:8080/swagger-ui.html

## Endpoints Principais

### Restaurantes

- `GET /restaurantes` - Listar restaurantes (filtro opcional)
- `GET /restaurantes/{id}` - Buscar restaurante por ID
- `POST /restaurantes` - Cadastrar restaurante
- `PUT /restaurantes/{id}` - Atualizar restaurante
- `DELETE /restaurantes/{id}` - Excluir restaurante

### Reservas

- `GET /reservas` - Listar reservas
- `GET /reservas/{id}` - Buscar reserva por ID
- `GET /reservas/usuario/{usuarioId}` - Listar reservas por usuário
- `POST /reservas` - Criar reserva
- `DELETE /reservas/{id}` - Cancelar reserva
- `PATCH /reservas/{id}/confirmar` - Confirmar reserva

### Avaliações

- `POST /avaliacoes` - Criar avaliação
- `GET /avaliacoes/restaurante/{restauranteId}` - Buscar avaliações por restaurante
- `GET /avaliacoes/usuario/{usuarioId}` - Buscar avaliações por usuário

### Usuários

- `POST /usuarios` - Cadastrar usuário
- `GET /usuarios/{id}` - Buscar usuário por ID
- `PUT /usuarios/{id}` - Atualizar usuário

## Testes

O projeto inclui testes unitários e de integração. Para executar os testes:

```bash
./mvnw test
```

## Construindo o Docker Image

Para construir a imagem Docker manualmente:

```bash
./mvnw clean package -DskipTests
docker build -t seu_usuario/gourmet:latest .
```

## CI/CD

O projeto está configurado com GitHub Actions para:

1. Executar testes em cada pull request (`maven-build.yml`)
2. Construir a imagem Docker em cada push para a branch principal (`docker-image.yml`)

## Solução de Problemas

- **Erro de conexão com o banco de dados**: Verifique se o PostgreSQL está rodando e acessível na porta 5432
- **Problemas de permissão ao executar o Maven Wrapper**: Execute `chmod +x ./mvnw` para dar permissão de execução

## Licença

Este projeto está licenciado sob a [Licença Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0).
