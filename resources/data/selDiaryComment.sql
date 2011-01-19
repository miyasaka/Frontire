select
	d1.mid,
	d1.diaryid,
	to_char(d2.entdate,'yyyymmdd') as yyyymmdd,
	(
		select count(*)
		from diary
		where
			    mid     = d1.mid
			and diaryid = d1.diaryid
			and comno  != 0
			and delflg  = '0'
	) as count,
	d2.title as l_title,
	case
		when length(d2.title) > 20 then substr(d2.title,0,21)||'・・・'
		else d2.title
	end as title,
	case
		when to_char(current_timestamp + '-1days','yyyymmdd') < to_char(max(d1.entdate),'yyyymmdd') then to_char(max(d1.entdate),'hh24:mi')
		else to_char(max(d1.entdate),'mm月dd日')
	end as entdate,
	case
		when to_char(current_timestamp + '-1days','yyyymmdd') < to_char(max(d1.entdate),'yyyymmdd') then to_char(max(d1.entdate),'hh24:mi')
		else to_char(max(d1.entdate),'mm/dd')
	end as entdatesla,
	case
		when to_char(current_timestamp + '-1days','yyyymmdd') < to_char(max(d2.entdate),'yyyymmdd') then to_char(max(d2.entdate),'hh24:mi')
		else to_char(max(d2.entdate),'mm月dd日')
	end as entdate2,
	case
		when to_char(current_timestamp + '-1days','yyyymmdd') < to_char(max(d2.entdate),'yyyymmdd') then to_char(max(d2.entdate),'hh24:mi')
		else to_char(max(d2.entdate),'mm/dd')
	end as entdate2sla,
	m.nickname
from
	diary d1,
	diary d2,
	members m
where
	(d1.mid,d1.diaryid) in (
		select mid,diaryid
		from diary
		where
			    updid   = /*mid*/
			and mid    != /*mid*/
			and entdate > (current_date - /*limitdate*/)
			and comno  != '0'
			and delflg  = '0'
		group by mid,diaryid
	)
	and d1.delflg  = '0'
	and d2.mid     = d1.mid
	and d2.diaryid = d1.diaryid
	and d2.comno   = '0'
	and d2.delflg  = '0'
	and m.mid      = d1.mid
	and m.status   = '1'
group by
	d1.mid,
	d1.diaryid,
	yyyymmdd,
	d2.title,
	m.nickname
/*IF sortcd =='01'*/
order by max(d1.entdate) desc
/*END*/
/*IF sortcd =='02'*/
order by max(d2.entdate) desc
/*END*/