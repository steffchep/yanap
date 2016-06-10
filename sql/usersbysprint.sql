CREATE TABLE "usersbysprint" (
	"id" BIGINT IDENTITY(1,1),
	"userid" BIGINT NOT NULL DEFAULT NULL,
	"sprintid" BIGINT NOT NULL DEFAULT NULL,
	"day01" DECIMAL(2,1) NOT NULL DEFAULT '0',
	"day02" DECIMAL(2,1) NOT NULL DEFAULT '0',
	"day03" DECIMAL(2,1) NOT NULL DEFAULT '0',
	"day04" DECIMAL(2,1) NOT NULL DEFAULT '0',
	"day05" DECIMAL(2,1) NOT NULL DEFAULT '0',
	"day06" DECIMAL(2,1) NOT NULL DEFAULT '0',
	"day07" DECIMAL(2,1) NOT NULL DEFAULT '0',
	"day08" DECIMAL(2,1) NOT NULL DEFAULT '0',
	"day09" DECIMAL(2,1) NOT NULL DEFAULT '0',
	"day10" DECIMAL(2,1) NOT NULL DEFAULT '0',
	PRIMARY KEY ("id")
)
;

INSERT INTO "usersbysprint" (userId, sprintId, day01, day02, day03, day04, day05, day06, day07, day08, day09, day10) VALUES (1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO "usersbysprint" (userId, sprintId, day01, day02, day03, day04, day05, day06, day07, day08, day09, day10) VALUES (2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO "usersbysprint" (userId, sprintId, day01, day02, day03, day04, day05, day06, day07, day08, day09, day10) VALUES (3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO "usersbysprint" (userId, sprintId, day01, day02, day03, day04, day05, day06, day07, day08, day09, day10) VALUES (4, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO "usersbysprint" (userId, sprintId, day01, day02, day03, day04, day05, day06, day07, day08, day09, day10) VALUES (5, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO "usersbysprint" (userId, sprintId, day01, day02, day03, day04, day05, day06, day07, day08, day09, day10) VALUES (6, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO "usersbysprint" (userId, sprintId, day01, day02, day03, day04, day05, day06, day07, day08, day09, day10) VALUES (7, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO "usersbysprint" (userId, sprintId, day01, day02, day03, day04, day05, day06, day07, day08, day09, day10) VALUES (8, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

