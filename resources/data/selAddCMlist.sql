-- コミュニティ・メンバー一覧表示用SQL
select
	m.mid,
	m.nickname,
	case coalesce(mp.mainpicno,'')
		when '1' then mp.pic1
		when '2' then mp.pic2
		when '3' then mp.pic3
	else ''
	end as pic
from
	community_enterant as c,
	members as m,
	memberphoto_info as mp
where
	    c.cid        = /*cid*/
	and c.joinstatus = '1'
	and m.mid        = c.mid
	and m.mid       <> /*mid*/
	and m.status     = '1'
	and m.mid        = mp.mid
order by m.mid