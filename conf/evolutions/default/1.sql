
# --- !Ups


CREATE TABLE "users"( "email" char(100) NOT NULL UNIQUE,"password" char (100) NOT NULL,"name" char(100) NOT NULL,"designation" char(100), "category" INT NOT NULL DEFAULT 0, "is_banned" BOOLEAN NOT NULL DEFAULT FALSE, "id" SERIAL PRIMARY KEY );

CREATE TABLE "sessions"("topic" char(100), "date" DATE , "slot" INT, "status" BOOLEAN, "user_id"  INT REFERENCES users("id"), "id" SERIAL PRIMARY KEY );

insert into "users" values('admin@gmail.com', 'cXdlcnR5', 'admin' , 'consultant',0, false,  1);

insert into "sessions" values( 'Spark' , '2016-08-25',1,false, 1, 1);

# --- !Downs

DROP TABLE "sessions";

DROP TABLE "users";




