SELECT
  A.ANO
 ,A.TITLE
 ,A.DETAIL
 ,A.PUBLEVEL
 ,TO_CHAR(A.ENTDATE,'YYYYMMDDHH24MISS') AS ENTDATE
 ,B.PIC
 ,B.PICNAME
 ,COALESCE(C.CNT,0) AS CNT
 ,COALESCE(D.CNT,0) AS UNREADCNT
 ,COALESCE(E.CNT,0) AS PHOTOCNT
FROM
  PHOTO_ALBUM A
  LEFT JOIN PHOTO B ON A.MID = B.MID AND A.ANO = B.ANO AND B.COVERFLG = '1' AND B.DELFLG = '0'
  LEFT JOIN (SELECT MID,ANO,COUNT(*) AS CNT
             FROM PHOTO_COMMENT
             WHERE
                  MID    = /*mid*/
              AND ANO    = /*ano*/
              AND DELFLG = '0'
             GROUP BY MID,ANO) C ON A.MID = C.MID AND A.ANO = C.ANO
  LEFT JOIN (SELECT MID,ANO,COUNT(*) AS CNT
             FROM PHOTO_COMMENT
             WHERE
                  MID     = /*mid*/
              AND ANO     = /*ano*/
              AND READFLG = '0'
              AND DELFLG  = '0'
             GROUP BY MID,ANO,READFLG) D ON A.MID = C.MID AND A.ANO = C.ANO
  LEFT JOIN (SELECT MID,ANO,COUNT(FNO) AS CNT
             FROM PHOTO
             WHERE
                  MID    = /*mid*/
              AND ANO    = /*ano*/
              AND DELFLG = '0'
             GROUP BY MID,ANO) E ON A.MID = E.MID AND A.ANO = E.ANO
WHERE
     A.MID    = /*mid*/
 AND A.ANO    = /*ano*/
 AND A.DELFLG = '0'