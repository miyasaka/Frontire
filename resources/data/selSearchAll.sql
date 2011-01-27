select 
 a.mid as firstid, 
 a.diaryid as secondid, 
 a.comno, 
 c.title, 
 a.comment, 
 a.pic1 as pic1, 
 a.pic2 as pic2, 
 a.pic3 as pic3, 
 to_char(a.entdate,'yyyymmddhh24miss') as entdate, 
 coalesce(a.picnote1,'') as picnote1, 
 coalesce(a.picnote2,'') as picnote2, 
 coalesce(a.picnote3,'') as picnote3, 
 'D' as entrytype, 
 case b.membertype when '1' then e.nickname || '[' || e.frontierdomain || ']' 
 else b.nickname end, 
 case b.membertype when '1' then e.pic else 
 case  
   d.mainpicno  
  when '1' then d.pic1 
  when '2' then d.pic2 
  when '3' then d.pic3 
  else null 
  end 
  end as pic, 
 c.cnt, 
 b.mid as writeid, 
 b.membertype, 
 e.frontierdomain, 
 e.fid 
from 
 diary as a, 
 members as b, 
 ( 
 select 
 b.mid, 
 b.diaryid, 
 b.comno, 
 b.title, 
 coalesce(a.cnt, 0) as cnt 
from 
 diary as b 
left join 
 ( 
  select 
   mid, 
   diaryid, 
   count(*) as cnt 
  from 
   diary 
  where 
   comno != 0 and 
   delflg = '0' 
  group by mid,diaryid 
 ) as a 
on 
 b.mid = a.mid and 
 b.diaryid = a.diaryid 
where 
 b.comno = 0 and 
 b.delflg = '0' 
) as c, 
memberphoto_info as d, 
frontier_user_management as e 
where 
 a.delflg = '0' and 
 b.status = '1' and 
 a.entid = b.mid and 
 a.mid = c.mid and 
 a.diaryid = c.diaryid and 
 b.mid = d.mid and 
 b.mid = e.mid and 
/*IF checkbox == "1"*/
 a.entid = /*mid*/ and
/*END*/
exists ( 
select z.diaryid from diary as z 
where 
(z.pub_diary = '1' 
/*IF groupcnt > 0*/
or (z.pub_diary = '2' and z.mid in /*grouplist*/(1,2)) 
/*END*/
or (z.pub_diary = '9' and z.mid = /*mid*/)) and
a.diaryid = z.diaryid and a.mid = z.mid and z.comno = 0) and
a.comment @@ /*searchword*/
union 
select 
 a.cid, 
 a.bbsid, 
 a.comno, 
 c.title, 
 a.comment, 
 a.pic1, 
 a.pic2, 
 a.pic3, 
 to_char(a.entdate,'yyyymmddhh24miss') as entdate, 
 coalesce(a.picnote1,'') as picnote1, 
 coalesce(a.picnote2,'') as picnote2, 
 coalesce(a.picnote3,'') as picnote3, 
 a.entrytype, 
 b.title, 
 coalesce(b.pic,null) as pic, 
 c.cnt, 
 null, 
 null, 
 null, 
 null 
from 
 community_bbs as a, 
 communities as b, 
( 
 select 
 b.cid, 
 b.bbsid, 
 b.comno, 
 b.title, 
 coalesce(a.cnt, 0) as cnt 
from 
 community_bbs as b 
left join 
 ( 
  select 
   cid, 
   bbsid, 
   count(*) as cnt 
  from 
   community_bbs 
  where 
   comno != 0 and 
   delflg = '0' 
  group by cid,bbsid 
 ) as a 
on 
 b.cid = a.cid and 
 b.bbsid = a.bbsid 
where 
 b.comno = 0 and 
 b.delflg = '0' 
) as c 
where 
 a.delflg = '0' and 
 a.cid = b.cid and 
a.cid = c.cid and 
a.bbsid = c.bbsid and 
c.comno = 0 and 
( 
     b.publevel = '0' 
 or ( 
     b.publevel != '0' 
     and  exists ( 
 select 
 ce.cid 
 from 
 community_enterant as ce 
 where 
     ce.cid = b.cid 
 and ce.mid = /*mid*/ 
 and joinstatus = '1' 
 )) 
 ) and 
/*IF checkbox == "1"*/
 a.mid = /*mid*/ and
/*END*/
a.comment @@ /*searchword*/
union 
select 
 a.cid, 
 null, 
 null, 
 a.title, 
 a.detail, 
 a.pic, 
 null, 
 null, 
 to_char(a.entdate,'yyyymmddhh24miss') as entdate, 
 '' as picnote1, 
 '' as picnote2, 
 '' as picnote3, 
 'C', 
 a.title, 
 coalesce(a.pic,null) as pic, 
 0, 
 null, 
 null, 
 null, 
 null 
from 
 communities as a 
where 
( 
     a.publevel = '0' 
 or ( 
     a.publevel != '0' 
     and  exists ( 
 select 
 ce.cid 
 from 
 community_enterant as ce 
 where 
     ce.cid = a.cid 
 and ce.mid = /*mid*/ 
 and joinstatus = '1' 
 )) 
 ) and 
/*IF checkbox == "1"*/
 a.admmid = /*mid*/ and
/*END*/
a.detail @@ /*searchword*/
union
select 
 a.mid as firstid, 
 null, 
 a.no as comno, 
 null, 
 a.comment, 
 null, 
 null, 
 null, 
 to_char(a.entdate,'yyyymmddhh24miss') as entdate, 
 '' as picnote1, 
 '' as picnote2, 
 '' as picnote3, 
 'S' as entrytype, 
 case b.membertype when '1' then e.nickname || '[' || e.frontierdomain || ']' 
 else b.nickname end, 
 case b.membertype when '1' then e.pic else 
  case  
   d.mainpicno  
  when '1' then d.pic1 
  when '2' then d.pic2 
  when '3' then d.pic3 
  else null 
  end 
  end as pic, 
 0, 
 b.mid as writeid, 
 b.membertype, 
 e.frontierdomain, 
 e.fid 
from 
 frontiershout as a, 
 members as b,
 memberphoto_info as d, 
 frontier_user_management as e 
where 
 a.delflg = '0' and 
 b.status = '1' and 
 a.mid = b.mid and 
 b.mid = d.mid and 
 b.mid = e.mid and 
exists ( 
select z.mid from frontiershout as z 
where 
(z.pub_level in ('0','1','2') 
/*IF groupcnt > 0*/
or (z.pub_level = '3' and z.mid in /*grouplist*/(1,2)) 
/*END*/
) and 
a.mid = z.mid and a.no = z.no) and 
/*IF checkbox == "1"*/
 a.mid = /*mid*/ and
/*END*/
a.comment @@ /*searchword*/
order by cnt desc,entdate desc,comno desc 