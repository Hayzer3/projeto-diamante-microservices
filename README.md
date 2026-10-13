# Diamante Delivery

Monorepo Maven de microsserviços para o Projeto Diamante. Requer JDK 21, Maven 3.6.3+ e Docker Compose.

## Integrantes

- Camily Vitoria Maciel — RM566520
- Eduarda Weiss — RM564434
- Lucas Nunes Soares — RM566503

## Serviços

| Serviço | Porta | Responsabilidade |
| --- | ---: | --- |
| eureka-server | 8761 | Descoberta de serviços |
| order-service | 8080 | Cardápio, pedidos, avaliações e assistente |
| payment-service | 8081 e 8082 | Pagamento simulado, falha em aproximadamente 50% das chamadas |
| review-service | 8083 | Consumo de avaliações, agregação e ranking |

## Como iniciar

```bash
docker compose up -d
mvn -pl eureka-server spring-boot:run
mvn -pl payment-service spring-boot:run
mvn -pl payment-service spring-boot:run -Dspring-boot.run.arguments=--server.port=8082
mvn -pl review-service spring-boot:run
mvn -pl order-service spring-boot:run
```

Execute cada comando Maven em um terminal separado, a partir da raiz do repositório. Todos os serviços usam `http://eureka-server:8761/eureka/` para descoberta. Como o enunciado executa as aplicações Maven no host, adicione `127.0.0.1 eureka-server` ao arquivo hosts do sistema antes de iniciá-las.

O assistente usa OpenAI quando `OPENAI_API_KEY` está definida. Para execução local compatível com OpenAI, configure `SPRING_AI_OPENAI_BASE_URL` (por exemplo, a URL do LM Studio ou Ollama) e `SPRING_AI_OPENAI_API_KEY` se o servidor exigir chave. Nenhuma chave é armazenada neste repositório. (Coloquei a chave nas variaveis de ambiente)

## Endpoints

- `GET /dishes`, `GET /dishes/{id}`
- `POST /orders` com `{ "dishId": 1, "quantity": 2 }`; `GET /orders/{id}`
- `POST /reviews` com `{ "dishId": 1, "rating": 5, "comment": "Great" }`
- `POST /assistant` com `{ "question": "Tem opção vegana?" }`
- `GET /reviews/ranking` no serviço de avaliações (porta 8083)
- `POST /payments` é interno e fica no serviço de pagamento

Os bancos H2 são em memória e cada serviço os mantém isolados. Dados de demonstração do cardápio são recriados ao iniciar `order-service`.
