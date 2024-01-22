# exploring-mongodb
Explorando o MongoDB

### Configurações da aplicação

* Java JDK 21
* MongoDB: 7.0.5
* Mongosh: 2.1.1

Obs: 

- Para rodar a aplicação, basta criar um banco chamado `student`, com o comando:

```
  use student;
```

- É preciso criar um usuário para o banco criado anteriormente, rode o seguinte comando:

```
db.createUser(
   {
     user: "root",
     pwd: "12345"
     roles:
       [
         { role: "readWrite", db: "student" }
       ]
   }
)
```

É possível checar se o usuário foi criado corretamente rodando os seguintes comandos:

```
use admin;
db.system.users.find()
```

## Configurações específicas do banco de dados:

* A coleção de estudantes segue as seguintes regras para a inserção de documentos:

```
db.runCommand({
   collMod: "student",
   validator: {
      $jsonSchema:{
         bsonType:"object",
         required:["name", "dateOfBirth"],
         properties:{
            name:{
               bsonType: "string",
               description: "student name is required"
            },
            dateOfBirth:{
               bsonType: "string",
               description: "student dateOfBirth is required"
            }
         }
      }
   },
   validationAction: "warn", 
   validationLevel: "moderate"
})
```

Obs:

- A utilização do `validationAction` como `warn` é para que o mongodb não retorne um erro para a aplicação, caso minha aplicação back-end envie dados que não correspodam as regras especificadas. E com o campo `validationLevel` como `moderate` a aplicação não persiste informações que não estejam definidas no esquema, somente gerando um log de violação da restrição especificada. (No presente momento, com os conhecimentos atuais, achei melhor seguir assim. Jogando parte da resposabilidade de validações para a aplicação back-end). 

### Endpoints disponíveis:


* Para cadastrar um documento com as informações de um estudante, basta mandar uma requisição `POST` para a url: `http://localhost:8085/api/student/create`, com o seguinte corpo: 

```
{
	"name": "Pedro Mirage Icety",
	"dateOfBirth": "2020-06-21"
}
```

* Para buscar o nome e idade de todos os estudantes, basta fazer uma requisição `GET` para a url: `http://localhost:8081/api/students`. O retorno esperado segue o seguinte formato:

```
{
	"students": [
		{
			"name": "Pedro Mirage Icety",
			"age": 3
		}
	]
}
``` 
