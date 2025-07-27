# Centro Comunitário 🔗
##Plataforma para facilitar trocas de recursos entre centros comunitários, promovendo eficiência e colaboração. 

## 🛠️ Tecnologias
- **Back-end**: Java, Spring Boot, Maven
- **Front-end**: Angular
- **Banco de dados**: MongoDB

## Principais funcionalidades:

- Listagem de centros comunitários;
- Relatórios (Média de recursos, Listagem de centro com ocupação crítica (90%+), Histórico de transações entre centros);
- Criação de intercâmbios (as ditas transações entre centros);
- Página inicial com listagem de páginas para fácil acesso;
- Vários testes unitários no back-end para garantir funcionamento adequado;

# INSTRUÇÕES

# ⚠ PARA ACESSO AO MONGODB ⚠
- Os dados do application properties estão no email para acesso.
- Crie a pasta resources e o file application.properties no path: centroComunitario > back-centro-comunitario > src > main>("resources > application.properties")

## Front
- Primeiro use "cd front-centro-comunitario";
- Então use "npm i" para instalar as dependências;
- E por fim use "ng serve" para rodar o projeto em angular.
- o acesso é no https://localhost:8080, ele vai redirecionar automaticamente pra homepage.

## Back
- Primeiro "cd back-centro-comunitario";
- Então use "mvn clean install" para buildar o target
- Então starte o projeto no ícone de play
- E o swagger deve estar no http://localhost:8080/swagger-ui/index.html

## Testes
- Primeiro "cd back-centro-comunitario";
- Então use "mvn test" et voilá!