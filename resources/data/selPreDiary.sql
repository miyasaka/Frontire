SELECT
    MAX(DIARYID) AS DIARYID,
    TO_CHAR(ENTDATE,'YYYYMMDD') AS PENTDATE
FROM
    DIARY
WHERE
    MID = /*mid*/ AND
    DIARYID < /*diaryid*/ AND
    COMNO = 0 AND
    DELFLG = '0' AND
	PUB_DIARY IN /*pubdiary*/(1, 2)
	/*IF membertype == "2" || membertype == "1"*/
		AND
		PUB_LEVEL IN /*publevel*/(1, 2) AND
		APP_STATUS IN /*appstatus*/(1, 2)
	/*END*/
GROUP BY DIARYID,ENTDATE
ORDER BY DIARYID DESC