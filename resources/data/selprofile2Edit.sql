select
 m.mid
,m.nickname
,coalesce(mp.pic1,'') as pic1
,coalesce(mp.pic2,'') as pic2
,coalesce(mp.pic3,'') as pic3
,coalesce(mp.mainpicno,'') as mainpicno
from
 members m
,memberphoto_info mp
where
 m.mid = mp.mid
and m.mid = /*mid*/