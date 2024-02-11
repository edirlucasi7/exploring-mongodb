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
db.system.users.find();
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

* Também foi criada uma restrição para a duplicidade de nomes para a coleção de estudantes:

```
db.student.createIndex({ name: 1 }, { unique: true })
```

Obs:

- A utilização do `validationAction` como `warn` é para que o mongodb não retorne um erro para a aplicação, caso minha aplicação back-end envie dados que não correspodam as regras especificadas. E com o campo `validationLevel` como `moderate` a aplicação não persiste informações que não estejam definidas no esquema, somente gerando um log de violação da restrição especificada. (No presente momento, com os conhecimentos atuais, achei melhor seguir assim. Jogando parte da resposabilidade de validações para a aplicação back-end).

## Falando um pouco sobre a modelagem:

* Para a coleção `student` temos as seguintes indormações:

![image](https://github.com/edirlucasi7/exploring-mongodb/assets/28410756/ce79570f-f1a0-40f9-b02d-7868e4908670)

* Para a coleção `subject` utilizamos a desnormalização dos dados, optando por replicar somente o nome dos estudantes matriculados na disciplina, além do `ObjectId` para garantir a consistência dos dados.

![image](https://github.com/edirlucasi7/exploring-mongodb/assets/28410756/a559ea11-a103-47e1-859e-1717241afd27)


### Endpoints disponíveis:


* Para cadastrar um documento com as informações de um estudante, basta mandar uma requisição `POST` para a url: `http://localhost:8085/api/student/create`, com o seguinte corpo: 

```
{
	"name": "Pedro Mirage Icety",
	"dateOfBirth": "2020-06-21"
}
```

Obs: Para o endpoint acima, foi implementao um "cache" em memória de todos os estudantes existentes na base para checar duplicidade de nomes, que é uma restrição imposta pelo banco de dados.

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

* Para atualizar um documento com as informações do estudante, basta mandar uma requisição `PUT` para a url: `http://localhost:8081/api/student/update/{id}`, com o seguinte corpo:

```
{
	"name": "Pedro Mirage Icety (Updated)",
	"dateOfBirth": "2020-06-21"
}
```

* Para deletar um documento com as informações do estudante, basta mandar uma requisição `DELETE` para a url: `http://localhost:8081/api/student/delete/{id}`, passando como parâmetro na url o ObjectId que referencia o documento.

* Para cadastrar um documento com as informações de disciplina e seus respectivos estudantes, basta mandar uma requisição `POST` para url: `http://localhost:8081/api/subject/create`, com o seguinte corpo:

```
{
	"name": "ANALISE E DESENVOLVIMENTO DE SISTEMAS",
	"code": 1718,
	"workload": 48,
	"studentsEnrollment": [
		{
			"studentId": "65b6a643ebc35069484e90e8"
		}
	]
}
```

Obs: O endpoint acima valida se todos os estudantes da disciplina em questão, realmente existem na base de dados e mapeia o nome de acordo com seu `ObjectId`. E também não permite mais de uma disciplina com o mesmo `code`.

* Para adicionar uma matrícula numa determinada disciplina, basta mandar uma requisição `PUT` para url: `http://localhost:8081/api/subject/{code}/add/students`, passando o código da disciplina e o seguinte corpo:

```
[
	{
		"studentId": "65b6a643ebc35069484e90e8"
	}	
]
```

Obs: O endpoint acima valida elementos nulos na lista, lista vazia, e define o parâmetro `studentId` como um atributo obrigatório. Não é permitido que alunos que não existam na coleção student sejam adicionaod numa disciplina.


* Para remover uma matrícula numa determinada disciplina, basta mandar uma requisição `PUT` para url: `http://localhost:8081/api/subject/{code}/remove/students`, passando o código da disciplina e o seguinte corpo:

```
[
	{
		"studentId": "65b6a643ebc35069484e90e8"
	}	
]
```

Obs: O endpoint acima valida elementos nulos na lista, lista vazia, e define o parâmetro `studentId` como um atributo obrigatório.

* Para listar todas as disciplinas, basta mandar uma requisição `GET` para url: `http://localhost:8081/api/subjects`

```
{
	"subjects": [
		{
			"id": "65c687f72bc6b95661cda24e",
			"name": "ANALISE E DESENVOLVIMENTO DE SISTEMAS",
			"code": 1718,
			"workload": 48,
			"createdAt": "2024-02-09",
			"studentsEnrollment": [
				{
					"studentId": "65c687dc2bc6b95661cda24d",
					"name": "Edir Lucas Icety",
					"enrollment": "2024-02-09"
				},
				{
					"studentId": "65c687c62bc6b95661cda24c",
					"name": "Pedro Mirage Icety",
					"enrollment": "2024-02-10"
				},
				{
					"studentId": "65c807a928b4400a3831397f",
					"name": "Mara Rubia Icety",
					"enrollment": "2024-02-10"
				}
			]
		}
	]
}
```

* Para remover uma disciplina, basta mandar uma requisição `DELETE` para url: `http://localhost:8081/api/subject/delete/{id}`, passando o `ObjectId` que referencia a disciplina.

```
{
	"subjects": [
		{
			"id": "65c687f72bc6b95661cda24e",
			"name": "ANALISE E DESENVOLVIMENTO DE SISTEMAS",
			"code": 1718,
			"workload": 48,
			"createdAt": "2024-02-09",
			"studentsEnrollment": [
				{
					"studentId": "65c687dc2bc6b95661cda24d",
					"name": "Edir Lucas Icety",
					"enrollment": "2024-02-09"
				},
				{
					"studentId": "65c687c62bc6b95661cda24c",
					"name": "Pedro Mirage Icety",
					"enrollment": "2024-02-10"
				},
				{
					"studentId": "65c807a928b4400a3831397f",
					"name": "Mara Rubia Icety",
					"enrollment": "2024-02-10"
				}
			]
		}
	]
}
```

