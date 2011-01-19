select
      c.cid,
      c.admmid,
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
	ce.mid        = /*admmid*/
	and ce.joinstatus = '1'
	and c.cid         = ce.cid
      

