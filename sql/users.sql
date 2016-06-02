CREATE TABLE "users" (
	"id" BIGINT NOT NULL DEFAULT NULL,
	"name" VARCHAR(50) NOT NULL DEFAULT NULL,
	"isDeveloper" BIT NOT NULL DEFAULT b'1',
	PRIMARY KEY ("id")
)
;

INSERT INTO "users" VALUES (1, 'Dawid', b'1');
INSERT INTO "users" VALUES (2, 'Jana', b'1');
INSERT INTO "users" VALUES (3, 'Jessica', b'1');
INSERT INTO "users" VALUES (4, 'Michael', b'1');
INSERT INTO "users" VALUES (5, 'Rauf', b'1');
INSERT INTO "users" VALUES (6, 'Steffi', b'1');
INSERT INTO "users" VALUES (7, 'Phil', b'0');
INSERT INTO "users" VALUES (8, 'Tom', b'0');
