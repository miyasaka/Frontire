SELECT
    A.MID,
    A.DIARYID,
    C.yyyymmdd,
    COUNT(A.COMNO)-1 AS COUNT,
    C.TITLE,
    TO_CHAR(MAX(A.ENTDATE),'YYYY年MM月DD日 hh24:mi') AS ENTDATE,
	TO_CHAR(MAX(C.ENTDATE),'YYYY年MM月DD日 hh24:mi') AS ENTDATE2,
    D.NICKNAME
FROM
    DIARY A,
    (SELECT
        mid,
        diaryid
    FROM
        diary
    WHERE
        updid =/*mid*/ AND
        mid!=/*mid*/ AND
        comno !='0' AND
		delflg ='0' AND
        to_char(entdate,'yyyymmdd') > /*day*/
    GROUP BY
        mid,
        diaryid
    ) B,
    (SELECT
        MID,
        TITLE,
        DIARYID,
        TO_CHAR(ENTDATE,'yyyymmdd') as yyyymmdd,
		ENTDATE
    FROM
        diary
    WHERE
        mid!=/*mid*/ AND
        comno ='0'
    ) C,
    MEMBERS D
WHERE
    A.MID = B.MID AND
    A.DIARYID = B.DIARYID AND
    B.MID = C.MID AND
    B.DIARYID = C.DIARYID AND
    A.DELFLG ='0' AND
    A.MID = D.MID AND
	D.STATUS ='1'
GROUP BY
    A.MID,
    A.DIARYID,
    C.yyyymmdd,
    C.TITLE,
    D.NICKNAME
ORDER BY MAX(A.ENTDATE) DESC