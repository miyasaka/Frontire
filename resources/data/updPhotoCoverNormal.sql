UPDATE
	PHOTO
SET
	COVERFLG = '0',
	UPDID = /*updid*/,
	UPDDATE = NOW()
WHERE
	MID = /*mid*/ AND
	ANO = /*ano*/ AND
	COVERFLG = '1'