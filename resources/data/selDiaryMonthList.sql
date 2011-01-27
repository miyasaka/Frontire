SELECT
 TO_CHAR(B.ENTDATE,'YYYYMMDD') AS ENTDATE
FROM
 DIARY AS B
WHERE
 B.MID=/*mid*/ AND
 B.COMNO = 0 AND
 B.DELFLG = '0' AND
 TO_CHAR(B.ENTDATE,'YYYYMM') = /*entdate*/ AND
 B.PUB_DIARY IN /*pubdiary*/(1, 2)
 /*IF membertype == "2" || membertype == "1"*/
  AND
  B.PUB_LEVEL IN /*publevel*/(1, 2) AND
  B.APP_STATUS IN /*appstatus*/(1, 2)
 /*END*/
GROUP BY TO_CHAR(B.ENTDATE,'YYYYMMDD') 
ORDER BY ENTDATE ASC