
# --- !Ups


CREATE TABLE users( email char(100) NOT NULL UNIQUE,password char (100) NOT NULL,name char(100) NOT NULL,designation char(100),id SERIAL PRIMARY KEY );

insert into users values('deepti@gmail.com', 'qwerty', 'deepti' , 'consultant', 1);



# --- !Downs


DROP TABLE "users";


