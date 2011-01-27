SELECT 
    A.SNO,
    A.TITLECOLOR,
    A.CID,
    A.BBSID,
    A.TITLE,
    A.DETAIL,
    A.STARTYEAR,
    A.STARTMONTH,
    A.STARTDAY,
    A.TIME_S,
    A.MINUTE_S,
    A.TIME_E,
    A.MINUTE_E,
    A.STARTTIME,
    A.ENDTIME,
    A.FRIENDSTATUS,
    A.NICKNAME,
	A.CTITLE,
    A.ENTRY
FROM (
SELECT
    S.SNO,
    S.TITLECOLOR,
    M.MID AS CID,
    NULL AS BBSID,
    S.TITLE,
    S.DETAIL,
    S.STARTYEAR,
    S.STARTMONTH,
	S.STARTDAY,
    S.STARTTIME AS TIME_S,
    S.STARTMINUTE AS MINUTE_S,
    S.ENDTIME AS TIME_E,
    S.ENDMINUTE AS MINUTE_E,
	CASE WHEN S.STARTTIME = '--' AND S.STARTMINUTE = '--' THEN NULL ELSE S.STARTTIME||':'||S.STARTMINUTE||' ～' END AS STARTTIME,
    CASE WHEN S.ENDTIME = '--' AND S.ENDMINUTE = '--' THEN NULL ELSE ' '||S.ENDTIME||':'||S.ENDMINUTE END AS ENDTIME,
	CASE WHEN S.MID = /*mid*/ THEN '0' WHEN S.MID IN /*idlist*/(1,2) THEN '1' ELSE '2' END AS FRIENDSTATUS,
	M.NICKNAME,
	NULL AS CTITLE,
    '2' AS ENTRY,
    S.ENTDATE
FROM
    SCHEDULE S,
    MEMBERS M
WHERE
	(
		(S.STARTYEAR = /*year*/ AND S.STARTMONTH=/*month*/) 
/*IF before_year != null*/
OR (S.STARTYEAR = /*before_year*/ AND S.STARTMONTH=/*before_month*/ AND S.STARTDAY >=/*before_day*/) 
/*END*/
/*IF next_year != null*/
OR (S.STARTYEAR = /*next_year*/ AND S.STARTMONTH=/*next_month*/ AND S.STARTDAY <=/*next_day*/)
/*END*/
) AND
	(S.MID = /*mid*/ OR
    ((S.MID IN /*idlist*/(1,2) AND S.PUBLEVEL IN ('1','2')) OR S.PUBLEVEL = '1')) AND
    S.MID = M.MID AND M.STATUS ='1'
UNION
SELECT
      NULL AS SNO,
      NULL AS TITLECOLOR,
	CE.CID,
      CB.BBSID,
	CB.TITLE,
      CB.COMMENT AS DETAIL,
      CI.EVENT_YEAR AS STARTYEAR,
      CI.EVENT_MONTH AS STARTMONTH,
      CI.EVENT_DAY AS STARTDAY,
      NULL AS TIME_S,
      NULL AS MINUTE_S,
      NULL AS TIME_E,
      NULL AS MINUTE_E,
      NULL AS STARTTIME,
      NULL AS ENDTIME,
      NULL AS FRIENDSTATUS,
      M.NICKNAME AS NICKNAME,
      C.TITLE AS CTITLE,
      '1' AS ENTRY,
	  CB.ENTDATE
FROM
	COMMUNITIES C,
	COMMUNITY_ENTERANT CE,
      COMMUNITY_BBS CB,
      COMMUNITY_EVENT_INFO CI,
	MEMBERS M
WHERE
	    CE.MID      = /*mid*/
	AND CE.JOINSTATUS = '1'
	AND C.CID         = CE.CID
      AND CE.CID        = CB.CID
      AND CB.CID        = CI.CID
      AND CB.BBSID      = CI.BBSID
	  AND CB.MID        = M.MID
      AND CB.COMNO      = '0'
      AND CB.ENTRYTYPE  = '2'
      AND CB.DELFLG     = '0' 
	  AND M.STATUS      = '1'
      AND ((CI.EVENT_YEAR = /*year*/ AND CI.EVENT_MONTH = /*month*/)
/*IF before_year != null*/
OR (CI.EVENT_YEAR = /*before_year*/ AND CI.EVENT_MONTH = /*before_month*/ AND CI.EVENT_DAY>=/*before_day*/)
/*END*/
/*IF next_year != null*/
OR (CI.EVENT_YEAR = /*next_year*/ AND CI.EVENT_MONTH = /*next_month*/ AND CI.EVENT_DAY<=/*next_day*/)
/*END*/
)
      
) A
ORDER BY A.STARTYEAR,A.STARTMONTH,A.STARTDAY,A.ENTRY,A.TIME_S,A.MINUTE_S,A.TIME_E,A.MINUTE_E,A.ENTDATE ASC