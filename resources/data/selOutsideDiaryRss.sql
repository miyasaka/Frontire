SELECT
 A.MID,
 A.DIARYID,
 A.COMNO,
 A.TITLE,
 TO_CHAR(A.ENTDATE,'YYYYMMDDHH24MISS') AS ENTDATE,
 A.APP_STATUS,
 A.DIARYCATEGORY,
 B.NICKNAME,
 B.FAMILYNAME
FROM
 DIARY AS A,
 MEMBERS AS B
WHERE
 exists (select c.mid from frontier_rss_member as c where id=/*id*/ and c.mid=a.mid) and
 A.COMNO = 0 AND
 A.DELFLG = '0' AND 
 A.APP_STATUS IN /*appstatus*/(1,2) AND
 A.MID = B.MID AND
 B.STATUS = '1'
ORDER BY A.ENTDATE DESC