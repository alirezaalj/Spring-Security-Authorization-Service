INSERT INTO ROLE(ID,NAME) VALUES
 (1,'ROLE_ADMIN'),
 (2,'ROLE_USER'),
 (3,'ROLE_SERVICE');

INSERT INTO USERS(ID,CREATED_AT,EMAIL,EMAIL_VERIFICATION
 ,ENABLE,LAST_LOGIN,PASSWORD,SERVICE_ACCESS,UPDATE_AT,USERNAME) values
 (1,'2021-09-09 00:00:00','admin@gmail.com',true ,true ,
 '2021-09-09 00:00:00','$2a$10$vJWfUw/MP2dDBuqOyQ6fKONxJeVwiQ3xCRQAg/eEyWfm8S6qsz36i',true ,'2021-09-09 00:00:00','admin'),
 (2,'2021-09-09 00:00:00','user1@gmail.com',true ,true ,
 '2021-09-09 00:00:00','$2a$10$yMl9RpO/vCALpZD0HU65leuteRp/QqTjYTIBbg2jvB.UFvISm7UAu',true ,'2021-09-09 00:00:00','user1'),
 (3,'2021-09-09 00:00:00','user2@gmail.com',false ,true ,
 '2021-09-09 00:00:00','$2a$10$8MfMQJ2ONd.HC5AN8idaJeuZ8I8FApIACREBueBlNXgC3uqUJRGtq',true ,'2021-09-09 00:00:00','user2'),
 (4,'2021-09-09 00:00:00','user3@gmail.com',true ,false ,
 '2021-09-09 00:00:00','$2a$10$uwq26LZBY8HE8uynf/54IehYleMRbGd0SyNyxU/nIBjRYBmwV0Cf6',true ,'2021-09-09 00:00:00','user3');

INSERT INTO USER_ROLE(USER_ID,ROLE_ID)
values (1,1),(1,2),(2,2),(3,2),(4,2);
