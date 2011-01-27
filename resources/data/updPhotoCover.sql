UPDATE
	PHOTO
SET
	COVERFLG = '1',
	UPDID = /*updid*/,
	UPDDATE = NOW()
WHERE
	MID = /*mid*/ AND
	ANO = /*ano*/ AND
	FNO = /*fno*/