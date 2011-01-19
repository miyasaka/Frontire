SELECT
  A.CNO
 ,A.COMMENT
 ,TO_CHAR(A.ENTDATE,'YYYYMMDDHH24MISS') AS ENTDATE
 ,B.MID
 ,B.NICKNAME
 ,B.STATUS
FROM
  PHOTO_COMMENT A
 ,MEMBERS B
WHERE
     A.MID    = /*mid*/
 AND A.ANO    = /*ano*/
 AND A.ENTID  = B.MID
 AND A.DELFLG = '0'
 /*IF cno != null*/
 AND A.CNO    IN /*cno*/(1,2)
 AND (A.MID   = /*memberId*/ 
      OR
      A.ENTID = /*memberId*/)
 /*END*/
ORDER BY CNO ASC