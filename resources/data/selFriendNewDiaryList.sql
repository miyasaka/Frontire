-- メンバー日記一覧
select
	a.mid,
	a.nickname,
	to_char(a.max_entdate,'yyyymmdd') as yyyymmdd,
	to_char(a.max_entdate,'yyyymmddhh24miss') as l_yyyymmdd,
	a.diaryid,
	a.comcnt as count,
	a.oya_title as title,
	case
		when a.yd < to_char(a.max_entdate,'yyyymmdd') then to_char(a.max_entdate,'hh24:mi')
		else to_char(a.max_entdate,'yy/mm/dd')
	end as entdatesla,
	case
		when a.yd < to_char(a.oya_entdate,'yyyymmdd') then to_char(a.oya_entdate,'hh24:mi')
		else to_char(a.oya_entdate,'yy/mm/dd')
	end as entdate2sla
from
	(
		select
			d1.mid,
			d1.diaryid,
			d1.title as oya_title,
			d1.entdate as oya_entdate,
			to_char(current_timestamp + '-1days','yyyymmdd') as yd,
			max(mm.nickname) as nickname,
			max(d2.entdate) as max_entdate,
			(
				select count(*)
				from diary
				where
					    mid     = d1.mid
					and diaryid = d1.diaryid
					and comno  != 0
					and delflg  = '0'
			) as comcnt
		from
			diary   d1,
			diary   d2,
			members mm
		where
			 mm.status                     = '1'
			and mm.mid                     != /*mid*/
			and mm.mid                     = d2.mid
			and d2.delflg                  = '0'
			and coalesce(d2.pub_diary,'') != '9'
			and d1.comno                   = 0
			and d1.mid                     = mm.mid
			and d1.delflg                  = '0'
			and d1.diaryid                 = d2.diaryid
			and ((mm.mid  IN /*idlist*/(1,2) AND d1.pub_diary !='9') 
					OR
				exists(
					select 
						f.followermid as mid 
					from 
						follow f
					where 
						f.followmid =/*mid*/ 
					and f.followermid = d1.mid 
					and d1.pub_diary = '1'
				)
				)
			 

		group by d1.mid,d1.diaryid,d1.title,d1.entdate
		/*IF sortcd =='01'*/
		order by max_entdate desc
		/*END*/
		/*IF sortcd =='02'*/
		order by oya_entdate desc
		/*END*/
	) a