SELECT
  CNO
FROM
  PHOTO_COMMENT
WHERE
     MID     = /*mid*/
 AND ANO     = /*ano*/
 AND DELFLG  = '0'
 AND CNO    IN /*cno*/(1,2)
 AND (MID    = /*memberId*/
      OR
      ENTID  = /*memberId*/)