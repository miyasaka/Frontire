SELECT
	COALESCE(MAX(ANO),0)+1 AS ANO
FROM
	PHOTO_ALBUM
WHERE
	MID = /*mid*/