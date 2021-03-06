SELECT
  MID
 ,ANO
 ,FNO
 ,PICNAME AS PICNAMEORG
 ,CASE
   WHEN LENGTH(PICNAME) > 29 THEN SUBSTR(PICNAME,0,30) || '…'
   ELSE PICNAME
  END AS PICNAME
 ,PIC
 ,COVERFLG
FROM
  PHOTO
WHERE
     MID    = /*mid*/
 AND ANO    = /*ano*/
 AND DELFLG = '0'
ORDER BY FNO ASC