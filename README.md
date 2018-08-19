# Money Transfer API

The REST Api for money transferring.

Technologies:

1. Maven
2. Web Framework SparkJava
3. Guice
4. JPA(Hibernate)
5. AssertJ, Mockito


### API

| Description | Request | Path | Response | Example |
| ------ | ------ | ------ | ------ | ------ |
| Create new account with balance | POST | /accounts | JSON with info about account | Input: { "balance": 100 }; Output: { "id": 1, "balance": 200} |
| Get account by id | GET | /accounts/{id} | JSON with info about account | Output: {"id":1,"balance":100.00,"name":"Nikolai"} |
 Get all accounts | GET | /accounts | JSON with info about accounts | Output: [{"id":1,"balance":100.00,"name":"Nikolai"},{"id":2,"balance":200.00,"name":"Pavel"}]
| Create transaction | POST | /transactions |JSON with info about transaction | Input: { "fromAccountId": 1, "toAccountId": 2, "amount": 200 }; Output: {"id":3,"fromAccountId":1,"toAccountId":2,"amount":200,"status":"DONE"} |
 Get transaction by id | GET | /transactions/{id} | JSON with info about transaction | Output: {"id":5,"fromAccountId":1,"toAccountId":2,"amount":200.00,"status":"DONE"} |
Get all transaction | GET | /transactions | JSON with info about transactions | Output:[{"id":5,"fromAccountId":1,"toAccountId":2,"amount":100.00,"status":"DONE"},{"id":6,"fromAccountId":1,"toAccountId":2,"amount":100.00,"status":"CANCELED"}] |
