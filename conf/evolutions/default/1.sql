
# --- !Ups


CREATE TABLE "users"( "email" char(100) NOT NULL UNIQUE,"password" char (100) NOT NULL,"name" char(100) NOT NULL,"designation" char(100), "category" INT NOT NULL DEFAULT 0, "id" SERIAL PRIMARY KEY );

CREATE TABLE "sessions"("topic" char(100) NOT NULL, "date" char(100), "user_id"  INT REFERENCES users("id"), "id" SERIAL PRIMARY KEY );

insert into "users" values('deepti@gmail.com', 'cXdlcnR5', 'deepti' , 'consultant',0, 1);

insert into "sessions" values( 'Spark' , '2016-07-15',1, 1);

# --- !Downs


DROP TABLE "sessions";

DROP TABLE "users";




