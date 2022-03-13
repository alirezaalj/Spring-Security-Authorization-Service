# Spring-Security-Authorization-Service
## Spring boot jwt security and email verification
This program is written using the spring boot framework and using spring security,which is used to create a database to store user information and send emails to complete user authentication, and how to authenticate users using a token that allows this project to You can easily run using the Docker service.
#### Details:
- Use gmail smtp
- Use jwt for authentication
- Having rolls for users
- Creating a professional and encrypted link for the types of services that we have used here to create an email authentication link
- Create an email template
- Send asymmetric emails

my website: [https://alirezaalijani.ir](https://alirezaalijani.ir "https://alirezaalijani.ir")
##### Goals
- ٍSecuring webservice
- Using spring data and mysql for save users data
- Java AES encryption for encrypting Objects and decrypting them
- Using jwt for api authentication
- Send email templates to registered users for authentication

# How to use

#### The first method method that you can user for running project
###### Project dependencies
- Docker : [Get Started](https://www.docker.com/ "Get Started")
- docker-compose : [Overview of Docker Compose](https://docs.docker.com/compose/ "Overview of Docker Compose")

#### Run
1. go to project folder open command line in there
2. start by this commands
```shell
mvn spring-boot:run
```

#### The second method that you can user for running project
###### Project dependencies

- maven  : [How to use or Download](https://maven.apache.org/ "How to use or Download")
- java 8 or higher
- gamil account or other smtp account
  if using gmail first do fallow this two step
  1- [Two Step Verification should be turned off.](https://support.google.com/accounts/answer/1064203?hl=en "Two Step Verification should be turned off.")
  2- [Allow Less Secure App(should be turned on).](https://myaccount.google.com/lesssecureapps "Allow Less Secure App(should be turned on).")
- mysql : *i suggest using docker*
  -- dockerhub : [https://hub.docker.com/_/mysql](https://hub.docker.com/_/mysql "https://hub.docker.com/_/mysql")
  -- create new database whit name : **service_user_security**
  -- using this commands for *pull *and use **mysql** whit docker

- run and pull mysql whit docker and config root password to **password**  on port **3306**
```shell
docker run --name=mysql-server  -e MYSQL_ROOT_PASSWORD=password  -p 3306:3306  -d mysql/mysql-server:latest
``` 
connect to mysql cli
```shell
docker exec -it  mysql-server mysql -uroot -ppassword
```
create new database
```sql
CREATE DATABASE service_user_security;
```
add your email info to **application.properties** file and host info
```
spring.mail.username=<YOURGAMIL@gmail.com>
spring.mail.password=<YOURGAMIL_PASSWPRD>
## if you run on domain like `alirezaaliani.ir`
service.emailDomainVerify=http://localhost:8080/api/auth/email/
```

#### Run
1. go to project folder
2. start by [spring-boot-maven-plugin](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/ "spring-boot-maven-plugin")

```shell
mvn spring-boot:run
```
3. project start on port 8080 and you can see the console for crud actions4. use this
4. command to create jar file

```shell
 mvn clean install
```
5. end

#### service api using
- send post request for create new user to [http://localhost:8080/api/auth/registration](http://localhost:8080/api/auth/registration "http://localhost:8080/api/auth/registration")
  whit body :
```json
{
   "email":"alirezaalijani.ir@gmail.com",
   "username":"alirezaalj",
   "password":"123456780",
   "verifyPassword":"123456780",
   "mobile":"09199558735"
}
```
and you get this response email send to alirezaalijani.ir@gmail.com
```json
{
   "status": "CREATED",
   "message": "registration is don now you can login to yot account"
}
```
postman :
[![postman](https://alirezaalijani.ir/assets/img/portfolio/Spring-boot-jwt-security-and-email-verification/1.png "postman")](https://alirezaalijani.ir/assets/img/portfolio/Spring-boot-jwt-security-and-email-verification/1.png "postman")
email :
[![email](https://alirezaalijani.ir/assets/img/portfolio/Spring-boot-jwt-security-and-email-verification/2.png "email")](https://alirezaalijani.ir/assets/img/portfolio/Spring-boot-jwt-security-and-email-verification/2.png "email")
