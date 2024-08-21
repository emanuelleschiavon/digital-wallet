### Realizando o processo de build

```
./gradlew clean build
```


### Rodando a aplicação

Depois de fazer o build da aplicação, rode para subir a aplicação e suas dependências:

```
docker-compose up -d
```

### Rodando testes unitários

Para rodar os testes unitários:

```
./gradlew test
```

### Rodando os testes de integração
Para rodar os testes de integração, primeiro certifique-se que as dependências do serviço não estão rodando:

```
docker-compose down
```

Por fim, rode os testes:

```
./gradlew integrationTest
```

### Criando uma transação

São 4 os endpoints disponíveis para criar transações dos 4 tipos de diferentes.

## Saque de conta

curl --location 'http://localhost:8080/transactions/withdraw' \
--header 'Content-Type: application/json' \
--data '{
"sourceAccountId": "123",
"date": "2020-03-01T22:00:00",
"value": 5
}'

## Depósito em conta

curl --location 'http://localhost:8080/transactions/deposit' \
--header 'Content-Type: application/json' \
--data '{
"sourceAccountId": "123",
"date": "2020-03-01T22:00:00",
"value": 5
}'

## Pagamento na conta

curl --location 'http://localhost:8080/transactions/payment' \
--header 'Content-Type: application/json' \
--data '{
"sourceAccountId": "123",
"date": "2020-03-01T22:00:00",
"value": 5
}'

## Transferência entre contas

curl --location 'http://localhost:8080/transactions/transfer' \
--header 'Content-Type: application/json' \
--data '{
"sourceAccountId": "123",
"targetAccountId": "456",
"date": "2020-03-01T22:00:00",
"value": 5
}'


### Arquitetura

![picpay.drawio.png](picpay.drawio.png)