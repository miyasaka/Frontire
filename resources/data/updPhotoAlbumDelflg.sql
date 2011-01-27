UPDATE PHOTO_ALBUM
SET
  DELFLG  = '1'
 ,UPDID   = /*updid*/
 ,UPDDATE = /*upddate*/
WHERE
     MID    = /*mid*/
 AND ANO    = /*ano*/
 AND DELFLG = '0'