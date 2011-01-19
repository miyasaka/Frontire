-- バッチ更新対象のmembersデータ取得
select
	m.mid,
	case coalesce(mp.mainpicno,'1')
		when '1' then coalesce(mp.pic1,'')
		when '2' then coalesce(mp.pic2,'')
		when '3' then coalesce(mp.pic3,'')
		else coalesce(mp.pic1,'')
	end as pic,
	m.nickname,
	m.status
from
	members m,
	memberphoto_info mp
where
	    m.membertype = '0'
	and m.mid        = mp.mid
	and to_char(m.upddate,'yyyy/mm/dd hh24:mi:ss') >= /*lastdate*/