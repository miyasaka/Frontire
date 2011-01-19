UPDATE
	DIARY
SET
	UPDDATE = NOW(),
	UPDID = /*updid*/,
	PUB_LEVEL = /*publevel*/,
	APP_STATUS = /*appstatus*/
WHERE
	MID = /*mid*/ AND
	DIARYID = /*diaryid*/ AND
	COMNO = 0