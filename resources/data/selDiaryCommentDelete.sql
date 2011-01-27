SELECT
 A.COMMENT,
 A.PIC1,
 A.PIC2,
 A.PIC3,
 A.COMNO,
 COALESCE(A.PICNOTE1,'') AS PICNOTE1,
 COALESCE(A.PICNOTE2,'') AS PICNOTE2,
 COALESCE(A.PICNOTE3,'') AS PICNOTE3,
 TO_CHAR(A.ENTDATE,'YYYYMMDDHH24MI') AS ENTDATE,
 B.NICKNAME,
 B.MID,
 B.STATUS,
 B.MEMBERTYPE,
 A.GUEST_NAME,
 A.GUEST_EMAIL,
 A.GUEST_URL,
 C.FRONTIERDOMAIN,
 C.FID,
 C.MID AS FMID,
 C.NICKNAME AS FNICKNAME,
 CASE 
  WHEN B.MEMBERTYPE = 0 THEN C.NICKNAME
  WHEN B.MEMBERTYPE = 1 THEN C.NICKNAME || ' [F]'
 ELSE ''
 END AS SFNICKNAME,
 CASE 
  WHEN B.MEMBERTYPE = 0 THEN C.NICKNAME
  WHEN B.MEMBERTYPE = 1 THEN C.NICKNAME || ' [' || C.FRONTIERDOMAIN || ']'
 ELSE ''
 END AS LFNICKNAME
FROM
 DIARY AS A,
 MEMBERS AS B,
 FRONTIER_USER_MANAGEMENT C
WHERE
 A.MID = /*mid*/ AND
 A.DIARYID = /*diaryid*/ AND
 A.COMNO IN /*comno*/(1,2) AND
 A.ENTID = C.MID AND
/*IF cid !=''*/
 B.MID = /*cid*/ AND
/*END*/
 A.ENTID = B.MID
ORDER BY A.COMNO ASC