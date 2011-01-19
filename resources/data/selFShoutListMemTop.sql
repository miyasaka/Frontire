SELECT
 A.MID,
 A.NICKNAME,
 A.NO,
 A.TWITTER,
 A.ENTID,
 A.ENTDATE,
 A.COMMENT,
 A.COMMENT AS COMMENTALL,
 CASE
  WHEN A.YD < TO_CHAR(A.ENTDATE,'YYYYMMDD') THEN TO_CHAR(A.ENTDATE,'HH24:MI')
  ELSE TO_CHAR(A.ENTDATE,'MM/DD')
  END AS ENTDATE,
 A.PHOTO,
 (SELECT COMMENT AS MYCOMMENT FROM FRONTIERSHOUT WHERE MID = /*mid*/ ORDER BY ENTDATE DESC LIMIT 1) AS MYCOMMENT,
  CASE WHEN A.ENTDATE_L < 5 THEN '約5分前'
   WHEN A.ENTDATE_L < 55 THEN '約'||(TRUNC(A.ENTDATE_L/5 + .9, 0)*5)||'分前'
   WHEN A.ENTDATE_L < 60*23 THEN '約'||CEIL(A.ENTDATE_L/60)||'時間前'
   WHEN A.ENTDATE_L < 60*24 THEN '1日以内'
   ELSE TO_CHAR(A.ENTDATE,'YYYY/MM/DD HH24:MI')
  END AS ENTDATEDATE,
  SUBSTRMID,
  A.FRONTIERDOMAIN,
  A.UPDID,
  A.UPDDATE,
  A.DEMANDFLG,
  A.CONFIRMFLG,
  A.PUB_LEVEL,
  A.DELFLG
FROM
(
SELECT
 M.MID,
 M.NICKNAME,
 FS.COMMENT,
 FS.NO,
 FS.TWITTER,
 FS.ENTID,
 FS.ENTDATE,
 TO_CHAR(CURRENT_TIMESTAMP + '-1DAYS','YYYYMMDD') AS YD,
 CASE mpi.MAINPICNO WHEN '1' THEN mpi.PIC1 WHEN '2' THEN mpi.PIC2 WHEN '3' THEN mpi.PIC3 END AS PHOTO,
 CASE TO_NUMBER(TO_CHAR(CURRENT_TIMESTAMP - FS.ENTDATE,'DDD'),'99999')
  WHEN 0 THEN TO_NUMBER(TO_CHAR(CURRENT_TIMESTAMP - FS.ENTDATE,'HH24'),'9999')*60+TO_NUMBER(TO_CHAR(CURRENT_TIMESTAMP - FS.ENTDATE,'MI'),'99')
  ELSE TO_NUMBER(TO_CHAR(CURRENT_TIMESTAMP - FS.ENTDATE,'DDD'),'99999')*60*24
  END AS ENTDATE_L,
 TO_NUMBER(SUBSTR(M.MID, 2, 10),'000000000') AS SUBSTRMID,
 FUM.FRONTIERDOMAIN,
 FS.UPDID,
 FS.UPDDATE,
 FS.DEMANDFLG,
 FS.CONFIRMFLG,
 FS.PUB_LEVEL,
 FS.DELFLG
FROM
 MEMBERS M,
 FRONTIERSHOUT FS,
 MEMBERPHOTO_INFO MPI,
 FRONTIER_USER_MANAGEMENT FUM
WHERE
     M.MID = /*mid*/
 AND M.MID = FS.MID
 AND M.MID = MPI.MID
 AND M.MID = FUM.MID
 AND FS.DELFLG                  = '0'
 --AND FS.ENTDATE > (CURRENT_DATE - /*limitdate*/)
GROUP BY
 M.MID,
 M.NICKNAME,
 FS.COMMENT,
 FS.NO,
 FS.TWITTER,
 FS.ENTID,
 FS.ENTDATE,
 PHOTO,
 FUM.FRONTIERDOMAIN,
 FS.UPDID,
 FS.UPDDATE,
 FS.DEMANDFLG,
 FS.CONFIRMFLG,
 FS.PUB_LEVEL,
 FS.DELFLG
ORDER BY FS.ENTDATE DESC
) A