select
	a.frontierdomain,
	a.gid,
	a.gname,
	m.mid,
	m.membertype,
	m.nickname,
	case mp.mainpicno
		when '1' then mp.pic1
		when '2' then mp.pic2
		when '3' then mp.pic3
		else ''
	end as pic,
	case
		when to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') not like '%day%' and substr(to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi'),1,2) = '00' then '03'
		when to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') not like '%day%' and substr(to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi'),1,2) <= 23 and substr(to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi'),1,2) > 0 then '02'
	end as lastlogin
from
	(
		SELECT
			fg.frontierdomain,
			fg.gid,
			fgj.mid,
			fg.gname
		FROM
			frontier_group fg,
			frontier_group_join fgj
		WHERE
			fg.gid            = /*gid*/ AND
			fg.frontierdomain = fgj.frontierdomain AND
			fg.gid            = fgj.gid AND
			fg.delflg         = '0' AND
			fg.frontierdomain = /*fdomain*/
	) a,
	members m,
	memberphoto_info mp
where
	    a.mid    = m.mid
	and m.status = '1'
	and m.mid    = mp.mid
order by a.frontierdomain,a.gid,m.lastaccessdate desc