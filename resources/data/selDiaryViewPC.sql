select
 a.diaryid,
 a.comno,
 a.title,
 a.comment,
 a.pic1,
 a.pic2,
 a.pic3,
 coalesce(a.picnote1,'') as picnote1,
 coalesce(a.picnote2,'') as picnote2,
 coalesce(a.picnote3,'') as picnote3,
 a.entdate,
 case b.membertype when '1' then z.nickname || ' [F]'
 else b.nickname end,
 b.status,
 b.mid,
 a.pubdiary,
 a.publevel,
 a.app_status,
 b.membertype,
 a.guest_name,
 a.guest_email,
 a.guest_url,
 c.comments,
 d.commentsoutside,
 case b.membertype when '1' then z.nickname || ' [' || z.frontierdomain || ']'
 else b.nickname end as nicknametitle,
 z.frontierdomain,
 z.fid
from
(
--日記本文を取得
select
 mid,
 diaryid,
 comno,
 title,
 comment,
 pic1,
 pic2,
 pic3,
 coalesce(picnote1,'') as picnote1,
 coalesce(picnote2,'') as picnote2,
 coalesce(picnote3,'') as picnote3,
 to_char(entdate,'yyyymmddhh24mi') as entdate,
 pub_diary as pubdiary,
 pub_level as publevel,
 app_status,
 guest_name,
 guest_email,
 guest_url,
 entid
from
 diary
where
 mid = /*mid*/ and
 diaryid = /*diaryid*/ and
 delflg = '0' and
 comno = 0 and
 pub_diary in /*pubdiary*/(1, 2)
 /*IF membertype == "2" || membertype == "1"*/
 and
 pub_level in /*publevel*/(1, 2) and
 app_status in /*appstatus*/(1, 2)
 /*END*/

union

--日記コメントを取得
select
 mid,
 diaryid,
 comno,
 title,
 comment,
 pic1,
 pic2,
 pic3,
 coalesce(picnote1,'') as picnote1,
 coalesce(picnote2,'') as picnote2,
 coalesce(picnote3,'') as picnote3,
 to_char(entdate,'yyyymmddhh24mi') as entdate,
 pub_diary as pubdiary,
 pub_level as publevel,
 app_status,
 guest_name,
 guest_email,
 guest_url,
 entid
from
 diary
where
 mid = /*mid*/ and
 diaryid = /*diaryid*/ and
 delflg = '0' and
 comno != 0
 /*IF membertype == "2"*/
 and
 pub_level in /*publevel*/(1, 2) and
 app_status in /*appstatus*/(1, 2)
 /*END*/
) as a,
 members as b,
 frontier_user_management as z,
(
 select
  count(*) as comments
 from
  diary
 where
  mid = /*mid*/
 and diaryid = /*diaryid*/
 and delflg = '0'
 and comno != '0'
) c,
(
 select
  count(*) as commentsoutside
 from
  diary
 where
  mid = /*mid*/
 and diaryid = /*diaryid*/
 and delflg = '0'
 and comno != '0'
 and pub_level = '0'
 and app_status = '4'
) d
where
 a.entid = b.mid and
 b.mid = z.mid 
order by a.comno asc