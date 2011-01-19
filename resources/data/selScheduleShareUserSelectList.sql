SELECT
	COALESCE(M.MID,'')    AS MID,
	COALESCE(M.NICKNAME,'')  AS NICKNAME
FROM
	MEMBERS M
WHERE
	M.STATUS       = '1'
	AND M.MID != /*mid*/
	AND M.MID IN /*idlist*/(1,2)
	AND NOT EXISTS
	(	SELECT 
		    SI.JOINMID
		FROM
		    SCHEDULE_JOIN_INFO SI
		WHERE
		    SI.MID = /*mid*/
		AND
		    SI.SNO = /*sno*/
		AND
		    SI.JOINMID = M.MID
		AND
		    DELFLG = '0'
		AND
		    M.STATUS = '1')
UNION
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
    DELFLG = '1'
AND
    M.STATUS = '1'