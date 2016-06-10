CREATE TABLE "users" (
	"id" BIGINT IDENTITY(1,1),
	"name" VARCHAR(50) NOT NULL DEFAULT NULL,
	"isDeveloper" BIT NOT NULL DEFAULT 1,
	"team" VARCHAR(50) NULL,
	PRIMARY KEY ("id")
)
;
INSERT INTO "users" (name, isDeveloper, team) VALUES ('Dawid', 1, 'Abraxas');
INSERT INTO "users" (name, isDeveloper, team) VALUES ('Jana', 1, 'Abraxas');
INSERT INTO "users" (name, isDeveloper, team) VALUES ('Jessica', 1, 'Abraxas');
INSERT INTO "users" (name, isDeveloper, team) VALUES ('Michael', 1, 'Abraxas');
INSERT INTO "users" (name, isDeveloper, team) VALUES ('Rauf', 1, 'Abraxas');
INSERT INTO "users" (name, isDeveloper, team) VALUES ('Steffi', 1, 'Abraxas');
INSERT INTO "users" (name, isDeveloper, team) VALUES ('Phil', 0, '');
INSERT INTO "users" (name, isDeveloper, team) VALUES ('Tom', 0, '');
