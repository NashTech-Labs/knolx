
# --- !Ups


CREATE TABLE users(id BIGINT PRIMARY KEY , email char(100) NOT NULL UNIQUE,password char (100) NOT NULL,name char(100) NOT NULL,address char(100) NOT NULL,designation char(100));

insert into users values(1, 'deepti@gmail.com', 'qwerty', 'deepti' , 'delhi', 'consultant');



# --- !Downs


DROP TABLE "users";


