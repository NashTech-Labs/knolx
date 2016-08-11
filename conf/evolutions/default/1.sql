
# --- !Ups


CREATE TABLE "users"( "email" char(100) NOT NULL UNIQUE,"password" char (100) NOT NULL,"name" char(100) NOT NULL,"designation" char(100), "category" INT NOT NULL DEFAULT 0, "id" SERIAL PRIMARY KEY );

CREATE TABLE "sessions"("topic" char(100), "date" DATE , "slot" INT, "status" BOOLEAN, "user_id"  INT REFERENCES users("id"), "id" SERIAL PRIMARY KEY );

CREATE TABLE "commitment"("uid" INT REFERENCES users("id"),"commit" INT , "done" INT , "id" SERIAL PRIMARY KEY );

insert into "users" values('admin@gmail.com', 'cXdlcnR5', 'admin' , 'consultant',0, 1);

insert into "sessions" values( 'Spark' , '2016-07-15',1,false, 1, 1);

INSERT INTO "commitment" VALUES (1,5,0,1)

# --- !Downs


DROP TABLE "sessions";

DROP TABLE "users";

DROP TABLE "commitment";




