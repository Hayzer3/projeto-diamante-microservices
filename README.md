# Projeto Diamante - Delivery

Monorepo Gradle de microsserviços para o Projeto Diamante. Requer JDK 25 e Docker Compose.

## Integrantes

- Camily Vitoria Maciel - RM566520
- Eduarda Weiss - RM564434
- Lucas Nunes Soares - RM566503

## Serviços

| Serviço | Porta | Responsabilidade |
| --- | ---: | --- |
| `eureka-server` | 8761 | Descoberta de serviços |
| `order-service` | 8080 | Cardápio, pedidos, avaliações e assistente |
| `payment-service` | 8081 e 8082 | Pagamento simulado, com falha em aproximadamente 50% das chamadas |
| `review-service` | 8083 | Consumo de avaliações, agregação e ranking |

## Pré-requisitos

- JDK 25 configurado para o Gradle.
- Docker Desktop com o mecanismo do Docker em execução.
- O nome `eureka-server` deve resolver para `127.0.0.1` no computador que executa os serviços. No Windows, adicione `127.0.0.1 eureka-server` ao arquivo `C:\Windows\System32\drivers\etc\hosts` com privilégios de administrador.

## Como iniciar

Na raiz do repositório, inicie o RabbitMQ:

```powershell
docker compose up -d
```

Depois, execute cada comando abaixo em um terminal separado. Inicie primeiro o Eureka e, em seguida, os demais serviços:

```powershell
.\gradlew.bat :eureka-server:bootRun
.\gradlew.bat :payment-service:bootRun
.\gradlew.bat :payment-service:bootRun --args="--server.port=8082"
.\gradlew.bat :review-service:bootRun
.\gradlew.bat :order-service:bootRun
```

No Linux ou macOS, execute `chmod +x gradlew` uma vez e use `./gradlew` no lugar de `gradlew.bat`. Na primeira execução, o Gradle pode precisar baixar a distribuição e as dependências.

## Assistente de IA

O endpoint `/assistant` precisa de uma chave configurada no ambiente que executa o `order-service`. No PowerShell, defina a variável antes de iniciar o serviço:

```powershell
$env:OPENAI_API_KEY="sua-chave"
.\gradlew.bat :order-service:bootRun
```

Também é possível configurar um provedor local compatível com a API da OpenAI usando `SPRING_AI_OPENAI_BASE_URL` e, se necessário, `SPRING_AI_OPENAI_API_KEY`. Nenhuma chave deve ser adicionada ao código ou ao repositório.

## Endpoints

### `order-service` - `http://localhost:8080`

- `GET /dishes` e `GET /dishes/{id}` - consulta o cardápio.
- `POST /orders` - cria um pedido. Exemplo: `{ "dishId": 1, "quantity": 2 }`.
- `GET /orders/{id}` - consulta um pedido.
- `POST /reviews` - publica uma avaliação. Exemplo: `{ "dishId": 1, "rating": 5, "comment": "Great" }`. Retorna `202 Accepted`.
- `POST /assistant` - pergunta ao assistente. Exemplo: `{ "question": "Tem opção vegana?" }`.

### `review-service` - `http://localhost:8083`

- `GET /reviews/ranking` - consulta o ranking de pratos por média das avaliações.

### `payment-service` - `http://localhost:8081` e `http://localhost:8082`

- `POST /payments` - endpoint interno usado pelo `order-service`; não é chamado pelo aplicativo mobile.

Os serviços de pedidos e avaliações usam bancos H2 em memória, isolados entre si. Os dados são perdidos quando cada serviço é reiniciado. O cardápio de demonstração é carregado ao iniciar o `order-service`.
