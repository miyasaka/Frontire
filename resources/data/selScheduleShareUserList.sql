SELECT 
    SI.JOINMID,
    M.NICKNAME
FROM
    SCHEDULE_JOIN_INFO SI,
    MEMBERS M
WHERE
    SI.MID = /*mid*/
AND
    SI.SNO = /*sno*/
AND
    SI.JOINMID = M.MID
AND
    DELFLG = '0'
AND
    M.STATUS = '1'