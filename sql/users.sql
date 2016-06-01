CREATE TABLE "users" (
	"id" BIGINT NOT NULL DEFAULT NULL,
	"name" VARCHAR(50) NOT NULL DEFAULT NULL,
	"isDeveloper" BIT NOT NULL DEFAULT b'1',
	PRIMARY KEY ("id")
)
;

INSERT INTO "users" VALUES (2, 'not Steffi', b'0');
INSERT INTO "users" VALUES (1, 'Steffi', b'1');
