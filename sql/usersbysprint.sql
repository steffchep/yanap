CREATE TABLE "usersbysprint" (
	"id" BIGINT NOT NULL DEFAULT NULL,
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

INSERT INTO "usersbysprint" VALUES (1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO "usersbysprint" VALUES (2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO "usersbysprint" VALUES (3, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO "usersbysprint" VALUES (4, 4, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO "usersbysprint" VALUES (5, 5, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO "usersbysprint" VALUES (6, 6, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO "usersbysprint" VALUES (7, 7, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO "usersbysprint" VALUES (8, 8, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

