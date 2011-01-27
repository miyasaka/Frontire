UPDATE
 DIARY
SET
 DELFLG = '1',
 UPDDATE = NOW(),
 UPDID = /*updid*/
WHERE
 MID = /*mid*/ AND
 DIARYID = /*diaryid*/ AND
 COMNO = /*comno*/