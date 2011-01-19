SELECT
  A.MID
 ,A.ANO
 ,A.FNO
 ,A.PICNAME
 ,A.PIC
-- ,TO_CHAR(A.ENTDATE,'YYYYMMDDHH24MISS') AS ENTDATE
-- ,TO_CHAR(A.UPDDATE,'YYYYMMDDHH24MISS') AS UPDDATE
 ,COALESCE(B.FNO,0) AS PREVFNO
 ,COALESCE(C.FNO,0) AS NEXTFNO
 ,D.PUBLEVEL
FROM
 PHOTO A
 LEFT JOIN (SELECT MID,ANO,MAX(FNO) AS FNO
            FROM PHOTO
            WHERE
                 MID    = /*mid*/
             AND ANO    = /*ano*/
             AND FNO    < /*fno*/
             AND DELFLG = '0'
            GROUP BY MID,ANO) B ON A.MID = B.MID AND A.ANO = B.ANO
 LEFT JOIN (SELECT MID,ANO,MIN(FNO) AS FNO
            FROM PHOTO
            WHERE
                 MID    = /*mid*/
             AND ANO    = /*ano*/
             AND FNO    > /*fno*/
             AND DELFLG = '0'
            GROUP BY MID,ANO) C ON A.MID = C.MID AND A.ANO = C.ANO
 ,PHOTO_ALBUM D
WHERE
     A.MID    = /*mid*/
 AND A.MID    = D.MID
 AND A.ANO    = /*ano*/
 AND A.ANO    = D.ANO
 AND A.DELFLG = '0'
 AND D.DELFLG = '0'
 AND A.FNO    = /*fno*/