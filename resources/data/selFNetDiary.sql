select
	a.mid,
	a.diaryid,
	a.comno,
	a.title,
	to_char(a.entdate,'yyyymmddhh24miss') as entdate,
	a.app_status,
	a.diarycategory,
	b.nickname
from
	diary as a,
	members as b
where
	    a.comno       = 0
	and a.mid        in /*midlist*/(1,2)
	and a.app_status in ('2','4')
	and a.delflg      = '0'
	and a.mid         = b.mid
	and b.status      = '1'
order by a.entdate desc