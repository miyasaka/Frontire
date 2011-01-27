 SELECT
     C.CID AS CMID,
     CB.MID AS MEMBERID,
     M.NICKNAME,
     C.BBSID,
     CB.ENTRYTYPE,
     CB.TITLE                             AS TITLE,
     CB.COMNO                             AS COMNO,
     C.TITLE                              AS COMMUNITY,
	 TO_CHAR(C.UPD,'yyyy年mm月dd日 hh24:mi') AS UPDATEDATE,
     CASE COALESCE(CB.PIC1,'')||COALESCE(CB.PIC2,'')||COALESCE(CB.PIC3,'') WHEN '' THEN '0' ELSE '1' END AS PHOTOS,
     C.COMMENTS-1                         AS COMMENTS
 FROM
     (SELECT
         CB.CID          AS CID,
         C.TITLE         AS TITLE,
         CB.BBSID        AS BBSID,
         COUNT(CB.COMNO) AS COMMENTS,
         MAX(CB.UPDDATE) AS UPD
     FROM
         COMMUNITIES C,
         COMMUNITY_ENTERANT CE,
         COMMUNITY_BBS CB
     WHERE
             CE.MID = /*mid*/
         AND CE.JOINSTATUS = '1'
         AND CE.CID = C.CID
         AND CE.CID = CB.CID
         AND CB.DELFLG = '0'
     GROUP BY C.TITLE,CB.CID,CB.BBSID) C,
     COMMUNITY_BBS CB,
     MEMBERS M
 WHERE
         C.CID = CB.CID
     AND CB.BBSID = C.BBSID
     AND CB.COMNO = '0'
     AND CB.DELFLG = '0'
     AND CB.MID = M.MID
     AND to_char(C.UPD,'yyyymmdd') > /*day*/
 ORDER BY C.UPD DESC,C.BBSID




