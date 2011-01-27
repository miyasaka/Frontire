SELECT
    TO_CHAR(A.ENTDATE,'YYYYMMDD') AS YYYYMMDD,
    A.DIARYID,
    A.TITLE,
    CASE
      WHEN length(A.TITLE) > 20 THEN SUBSTR(A.TITLE,0,21) || '・・・'
      ELSE A.TITLE
    END AS STITLE,
    B.COMCNT
FROM
    DIARY A,
    (SELECT
        DIARYID,
        MIN(ENTDATE) AS ENTDATE,
        MID,
        COUNT(MID) AS COMCNT
    FROM
        DIARY
    WHERE
        MID     = /*mid*/ AND
        DELFLG  = '0' AND
        READFLG = '0' AND
        COMNO  != '0' AND
        ENTID  != /*mid*/
    GROUP BY
        DIARYID,
        MID
    ) B
WHERE
    A.DIARYID = B.DIARYID AND
    A.MID     = B.MID AND
    A.COMNO   = '0'
ORDER BY
    B.ENTDATE
