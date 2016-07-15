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
	"day11" DECIMAL(2,1) NULL DEFAULT NULL,
	"day12" DECIMAL(2,1) NULL DEFAULT NULL,
	"day13" DECIMAL(2,1) NULL DEFAULT NULL,
	"day14" DECIMAL(2,1) NULL DEFAULT NULL,
	"day15" DECIMAL(2,1) NULL DEFAULT NULL,
	"day16" DECIMAL(2,1) NULL DEFAULT NULL,
	"day17" DECIMAL(2,1) NULL DEFAULT NULL,
	"day18" DECIMAL(2,1) NULL DEFAULT NULL,
	"day19" DECIMAL(2,1) NULL DEFAULT NULL,
	"day20" DECIMAL(2,1) NULL DEFAULT NULL,
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

