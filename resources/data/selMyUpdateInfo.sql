select
	a.mid,
	a.ano,
	a.diaryid,
	a.l_title,
	a.title,
	coalesce(a.entdate,'') as ent,
	coalesce(a.entdate2,'') as upd,
	a.comments,
	a.type
from
	(
		select
			pa.mid,
			pa.ano,
			0 as diaryid,
			pa.title as l_title,
			case
				when length(pa.title) > 10 then substr(pa.title,0,11)||'・・・'
				else pa.title
			end as title,
			to_char(pa.entdate,'yyyymmddhh24miss') as entdate,
			case
				when pc.entdate is null then to_char(pa.entdate,'yyyymmddhh24miss')
				else to_char(pc.entdate,'yyyymmddhh24miss')
			end as entdate2,
			(
				select count(*)
				from photo_comment
				where
					    mid = /*mid*/
					and ano = pa.ano
					and delflg = '0'
			) as comments,
			'1' as type
		from
			photo_album pa
			left join (
				select
					mid,
					ano,
					max(entdate) as entdate
				from
					photo_comment
				where
					    mid    = /*mid*/
					and entdate > (current_date - /*limitdate*/)
					and delflg = '0'
				group by
					mid,ano
				) pc
			on
				    pa.mid = pc.mid
				and pa.ano = pc.ano
		where
			    pa.mid    = /*mid*/
			and pa.entdate > (current_date - /*limitdate*/)
			and pa.delflg = '0'

	union

		select 
                d1.mid,
                '0' as ano,
                d1.diaryid,
                d2.title as l_title,
                case when length(d2.title) > 10 then substr(d2.title,0,11)||'・・・'
            			else d2.title end as title,
                to_char(d2.entdate,'yyyymmddhh24miss') as entdate,
                to_char(max(d1.upddate),'yyyymmddhh24miss') as entdate2,
            
            (
            				select count(*)
            				from diary
            				where
            					    mid     = d1.mid
            					and diaryid = d1.diaryid
            					and comno  != 0
            					and delflg  = '0'
            			) as comments,
                '2' as type
            from
                diary d1,
                diary d2
            where
                d1.mid = /*mid*/ and
                d1.diaryid = d2.diaryid and
                d1.mid = d2.mid and 
                d1.upddate > (current_date - /*limitdate*/) and
                d2.comno = '0' and
                d1.delflg ='0' and
                d2.delflg = '0'
            group by 
                d1.diaryid,d1.mid,d2.entdate,d2.title
	) a
/*IF sortcd == '01'*/
order by upd desc
/*END*/
/*IF sortcd == '02'*/
order by ent desc
/*END*/