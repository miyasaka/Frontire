UPDATE PHOTO_COMMENT
SET
  DELFLG  = '1'
 ,UPDID   = /*updid*/
 ,UPDDATE = /*upddate*/
WHERE
     MID    = /*mid*/
 AND ANO    = /*ano*/
 AND DELFLG = '0'
 /*IF cno != null*/
 AND CNO    = /*cno*/
 /*END*/