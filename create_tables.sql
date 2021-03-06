CREATE TABLE "sprints" (
	"id" BIGINT NOT NULL DEFAULT NULL,
	"name" VARCHAR(255) NOT NULL DEFAULT NULL,
	"startDate" DATE NOT NULL DEFAULT NULL,
	"endDate" DATE NOT NULL DEFAULT NULL,
	"status" INT NOT NULL DEFAULT '1',
	PRIMARY KEY ("id")
)
;

CREATE TABLE "users" (
	"id" BIGINT NOT NULL DEFAULT NULL,
	"name" VARCHAR(50) NOT NULL DEFAULT NULL,
	"isDeveloper" BIT NOT NULL DEFAULT b'1',
	PRIMARY KEY ("id")
)
;

CREATE TABLE "usersbysprint" (
	"id" BIGINT NOT NULL DEFAULT NULL,
	"userid" BIGINT NOT NULL DEFAULT NULL,
	"sprintid" BIGINT NOT NULL DEFAULT NULL,
	"day01" INT NOT NULL DEFAULT '0',
	"day02" INT NOT NULL DEFAULT '0',
	"day03" INT NOT NULL DEFAULT '0',
	"day04" INT NOT NULL DEFAULT '0',
	"day05" INT NOT NULL DEFAULT '0',
	"day06" INT NOT NULL DEFAULT '0',
	"day07" INT NOT NULL DEFAULT '0',
	"day08" INT NOT NULL DEFAULT '0',
	"day09" INT NOT NULL DEFAULT '0',
	"day10" INT NOT NULL DEFAULT '0',
	PRIMARY KEY ("id")
)
;

