INSERT INTO visitors
(
	mid,
	visitday,
	visitmid,
	lastvisittime,
	visitcount
)
VALUES
(
	/*mid*/,
	to_char(current_timestamp,'yyyymmdd'),
	/*visitmid*/,
	current_timestamp,
	1
);