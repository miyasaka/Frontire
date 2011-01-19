UPDATE PHOTO
SET
  DELFLG  = '1'
 ,UPDID   = /*updid*/
 ,UPDDATE = /*upddate*/
WHERE
     MID    = /*mid*/
 AND ANO    = /*ano*/
 AND DELFLG = '0'
 /*IF fno != null*/
 AND FNO    = /*fno*/
 /*END*/