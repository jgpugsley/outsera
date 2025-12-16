# Outsera - API de Premiações de Cinema

Este projeto é uma API REST desenvolvida para ler dados da lista de indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards a partir de arquivos CSV e processar estatísticas sobre os produtores, identificando aqueles com o maior e menor intervalo entre prêmios consecutivos.
 

## 🚀 Tecnologias Utilizadas

  * **Java 17**
  * **Spring Boot 4**
  * **Spring Data JPA**
  * **H2 Database** (Banco de dados em memória para execução rápida)
  * **Maven** (Gerenciador de dependências)

## 📋 Funcionalidades

  * **Carga de Dados**: Processamento automático de arquivos CSV na inicialização ou via upload.
  * **Análise de Intervalos**: Identificação do produtor com o maior intervalo entre dois prêmios e o produtor que obteve dois prêmios mais rápido.
  * **API REST**: Endpoints para consulta e processamento dos resultados.

## 🔧 Como Executar o Projeto

### Pré-requisitos

  * JDK 17 ou superior.
  * Maven instalado (opcional, pode usar o `mvnw` incluso).

### Passo a Passo

1.  **Clonar o repositório**:
    ```bash
    git clone https://github.com/jgpugsley/outsera.git
    ```
2.  **Entrar na pasta**:
    ```bash
    cd outsera
    ```
3.  **Executar a aplicação**:
    ```bash
    ./mvnw spring-boot:run
    ```

## 📁 Estrutura de Pastas

  * `src/main/java`: Código fonte da aplicação.
  * `src/main/resources`: Arquivos de configuração e o arquivo CSV de dados.
  * `src/test/java`: Testes de integração e unitários.

## 🔗 Endpoints da API

### 1\. Upload de Arquivo CSV

Utilizado para carregar uma nova lista de filmes e produtores.

  * **URL:** `http://localhost:8080/upload`

**Exemplo de formato CSV aceito:**

```csv
ano;produtor;venceu;nome
1980;Allan Carr;yes;Can't Stop the Music
1980;Lawrence Gordon;no;Flash Gordon
2008;Producer 1;yes;Film A
2009;Producer 1;yes;Film B
```

### 2\. Obter Intervalos de Prêmios

Retorna os produtores com o maior e o menor intervalo entre dois prêmios consecutivos.

  * **URL:** `http://localhost:8080/api/producers/intervals`
  * **Método:** `GET`
  * **Resposta de Sucesso (200 OK):**

<!-- end list -->

```json
{
  "min": [
    {
      "producer": "Producer 1",
      "interval": 1,
      "previousWin": 2008,
      "followingWin": 2009
    }
  ],
  "max": [
    {
      "producer": "Producer 2",
      "interval": 99,
      "previousWin": 1900,
      "followingWin": 1999
    }
  ]
}
```

## 🛠️ Como os dados são carregados?

  * **Inicialização**: O sistema lê automaticamente o arquivo `movielist.csv` em `src/main/resources`.
  * **Persistência**: Os dados são normalizados e salvos no banco de dados **H2** em memória.
  * **Processamento**: A lógica utiliza JPA e Java Streams para organizar os vencedores por ano e calcular as diferenças de tempo.

## 🧪 Testes de Integração

O projeto possui testes automatizados que validam a carga do CSV e a exatidão dos cálculos de intervalo.

**Para rodar os testes:**

```bash
./mvnw test
```

## 🚀 Testando com Postman

Incluímos uma Collection para facilitar seus testes.

1.  Localize a pasta `/postman` na raiz do projeto.
2.  No Postman, clique em **Import**.
3.  Selecione o arquivo `Outsera_API.postman_collection.json`.

## ✒️ Autor

  * **Joao Pugsley** - [jgpugsley](https://www.google.com/search?q=https://github.com/jgpugsley)
