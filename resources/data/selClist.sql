-- コミュニティ一覧表示用SQL
select
	ce.mid,
	ce.cid,
	c.title,
	c.pic,
	case when ce.mid = c.admmid then '1'
	else '0'
	end as admflg,
	(
		select
			count(cecnt.*)
		from
			community_enterant cecnt,
			members m
		where
			    cecnt.cid = c.cid
			and cecnt.mid = m.mid
			and cecnt.joinstatus = '1'
			and m.status  = '1'
	) as memcnt
from
	communities c,
	community_enterant ce
where
	    ce.mid        = /*listcid*/
	and ce.joinstatus = '1'
	and c.cid         = ce.cid
order by admflg desc,ce.entdate desc;