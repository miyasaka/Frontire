select 
    d.mid
from
    diary d,
    follow f
where
    f.followmid = /*mid*/
and
    f.followermid = d.mid
and
    d.comno = '0'
and
    d.delflg = '0'
and
    d.diaryid = /*diaryid*/
and
    d.pub_diary = '1'
and
    d.mid = /*fid*/