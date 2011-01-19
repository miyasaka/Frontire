SELECT 
    A.SNO,
    A.TITLECOLOR,
    A.CID,
    A.BBSID,
    A.TITLE,
    A.DETAIL,
    REPLACE(A.DETAIL,'\r\n','\\n') AS DETAILJS,
    A.STARTYEAR,
    A.STARTMONTH,
    A.STARTDAY,
    A.STARTYEAR||A.STARTMONTH||A.STARTDAY AS SCHEDULEDATE,
    A.TIME_S,
    A.MINUTE_S,
    A.TIME_E,
    A.MINUTE_E,
    A.STARTTIME,
    A.ENDTIME,
    A.FRIENDSTATUS,
    A.NICKNAME,
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
    '2' AS ENTRY,
    S.ENTDATE
FROM
    SCHEDULE S,
    MEMBERS M
WHERE
    S.STARTYEAR||S.STARTMONTH||S.STARTDAY BETWEEN /*startdate*/ AND /*enddate*/
 AND
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
      AND CI.EVENT_YEAR||CI.EVENT_MONTH||CI.EVENT_DAY BETWEEN /*startdate*/ AND /*enddate*/
) A
ORDER BY A.STARTYEAR,A.STARTMONTH,A.STARTDAY,A.ENTRY,A.TIME_S,A.MINUTE_S,A.TIME_E,A.MINUTE_E,A.ENTDATE ASC