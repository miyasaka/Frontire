-- コミュニティ検索
select
	c.cid,
	c.title,
	c.detail,
	c.pic,
	c.category1,
	mc.itemname,
	(
		select
			count(ce.*)
		from
			community_enterant ce,
			members m
		where
			    ce.cid        = c.cid
			and ce.mid        = m.mid
			and ce.joinstatus = '1'
			and m.status      = '1'
	) as memcnt,
	to_char(c.entdate,'yyyymmdd') as entdate
from
	communities as c,
	mst_comitemcode as mc
where
	    mc.itemid   = 'category'
	and c.category1 = mc.itemcd
/*IF title != null && title != ""*/
	and c.title like '%' || /*title*/ || '%' ESCAPE '#'
/*END*/
/*IF detail != null && detail != ""*/
	and c.detail like '%' || /*detail*/ || '%' ESCAPE '#'
/*END*/
order by c.entdate desc