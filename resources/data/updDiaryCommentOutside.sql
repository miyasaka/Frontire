-- 日記コメント外部公開の更新
UPDATE DIARY
SET
	PUB_LEVEL = /*publevel*/,
	APP_STATUS = /*appstatus*/,
	UPDDATE = NOW(),
	UPDID = /*updid*/
WHERE
	MID = /*mid*/ AND
	DIARYID = /*diaryid*/ AND
	COMNO = /*comno*/
