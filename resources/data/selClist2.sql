-- イベント作成可能コミュニティ一覧表示用SQL
select
	ce.mid,
	ce.cid,
	c.title as LTITLE,
	CASE WHEN LENGTH(C.TITLE) > 15 THEN SUBSTR(C.TITLE,0,16)||'・・・' ELSE C.TITLE END AS TITLE,
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
    and ((ce.mid = c.admmid) or c.makabletopic = '0')
order by admflg desc,ce.entdate desc;