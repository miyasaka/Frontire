SELECT
 B.DIARYID,
 B.TITLE,
 B.COMMENT,
 TO_CHAR(B.ENTDATE,'YYYYMMDDHH24MI') AS ENTDATE,
 COALESCE(
  (
   SELECT COUNT(*)
   FROM DIARY
   WHERE
    MID=/*mid*/ AND
    MID=B.MID AND
    DIARYID=B.DIARYID AND
    COMNO != 0 AND
    DELFLG = '0')
 , 0) AS CNT,
 COALESCE(
  (
   SELECT COUNT(*)
   FROM DIARY
   WHERE
    MID=/*mid*/ AND
    MID=B.MID AND
    DIARYID=B.DIARYID AND
    COMNO != 0 AND
    PUB_LEVEL = '0' AND
    APP_STATUS = '4' AND
    DELFLG = '0')
 , 0) AS CNTOUTSIDE,
 B.PIC1,
 B.PIC2,
 B.PIC3,
 COALESCE(B.PICNOTE1,'') AS PICNOTE1,
 COALESCE(B.PICNOTE2,'') AS PICNOTE2,
 COALESCE(B.PICNOTE3,'') AS PICNOTE3
FROM
 DIARY AS B
WHERE
 B.MID=/*mid*/ AND
 B.COMNO = 0 AND
 B.DELFLG = '0' AND
 B.PUB_DIARY IN /*pubdiary*/(1, 2)
 /*IF membertype == "2" || membertype == "1"*/
  AND
  B.PUB_LEVEL IN /*publevel*/(1, 2) AND
  B.APP_STATUS IN /*appstatus*/(1, 2)
 /*END*/
 /*IF searchMonth != null*/
  AND
  TO_CHAR(B.ENTDATE,'YYYYMM') = /*searchMonth*/
 /*END*/
 /*IF searchDay != null*/
  AND
  TO_CHAR(B.ENTDATE,'YYYYMMDD') = /*searchDay*/
 /*END*/
ORDER BY B.ENTDATE DESC