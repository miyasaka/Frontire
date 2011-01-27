UPDATE
 visitors
SET
	lastvisittime = current_timestamp,
	visitcount = visitcount + 1
WHERE
	    mid = /*mid*/
	and visitmid = /*visitmid*/
	and visitday = to_char(current_timestamp,'yyyymmdd')