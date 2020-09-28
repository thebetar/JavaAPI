BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "blogs" (
	"_id"	VARCHAR(255),
	"title"	VARCHAR(255),
	"description"	MEDIUMTEXT,
	"date"	date,
	"category"	VARCHAR(256)
);
INSERT INTO "blogs" VALUES ('8f14331a-ee5f-475f-8fd9-9afb8dda7050','Rainy day','Today was a rainy day','2020-09-30','Work');
INSERT INTO "blogs" VALUES ('97244a4f-e03d-4f44-9780-9fbf0666061d','Snowy day','It was very very snowy today','2020-09-26','Holiday');
INSERT INTO "blogs" VALUES ('b1ea76d7-79d8-4f06-9578-f02e2d6e59e5','Windy day','Today was a very windy day, it did not like it one bit!','2020-09-25','Weather');
INSERT INTO "blogs" VALUES ('43d70b09-a0da-4127-8791-a6cd2f9b468d','Going to the beach','It went to the beach today, it was wonderful','2020-09-21','Holiday');
COMMIT;
