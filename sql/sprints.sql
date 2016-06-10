CREATE TABLE "sprints" (
	"id" BIGINT IDENTITY(1,1),
	"name" VARCHAR(255) NOT NULL,
	"startDate" DATE NOT NULL DEFAULT NULL,
	"endDate" DATE NOT NULL DEFAULT NULL,
	"status" INT NOT NULL DEFAULT '1',
	"team" VARCHAR(50) NOT NULL DEFAULT '',
	"pointsplanned" INT NOT NULL DEFAULT '0',
	"pointscompleted" INT NOT NULL DEFAULT '0',
	PRIMARY KEY ("id")
)
;

INSERT INTO "sprints" (name, startDate, endDate, status, team, pointsplanned, pointscompleted) VALUES ('My First Sprint', '2016-05-17', '2016-05-19', 1, 'Abraxas', 0, 0);