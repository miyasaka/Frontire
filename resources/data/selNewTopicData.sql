select
	a.cid as cmid,
	a.mid as memberid,
	(select nickname from members where mid = a.mid) as nickname,
	a.bbsid,
	a.entrytype,
	a.bbstitle as title,
	a.comno,
	a.cname as community,
	case
		when to_char(current_timestamp + '-1days','yyyymmdd') < to_char(a.maxupddate,'yyyymmdd') then to_char(a.maxupddate,'hh24:mi')
		else to_char(a.maxupddate,'yy/mm/dd')
	end as updatedate2,
	case
		when to_char(current_timestamp + '-1days','yyyymmdd') < to_char(a.oyaentddate,'yyyymmdd') then to_char(a.oyaentddate,'hh24:mi')
		else to_char(a.oyaentddate,'yy/mm/dd')
	end as entdate2,
	a.comments
from
	(
		select
			c.cid,
			cb1.bbsid,
			cb1.comno,
			cb1.mid,
			cb1.title as bbstitle,
			c.title as cname,
			cb1.entdate as oyaentddate,
			cb1.entrytype,
			max(cb2.upddate) as maxupddate,
			(
				select count(*)
				from community_bbs
				where
					    cid    = c.cid
					and bbsid  = cb1.bbsid
					and comno != 0
					and delflg = '0'
			) as comments
		from
			communities c,
			community_enterant ce,
			community_bbs cb1,
			community_bbs cb2
		where
			    ce.mid        = /*mid*/
			and ce.joinstatus = '1'
			and ce.cid        = c.cid
			and cb1.cid       = c.cid
			and cb1.comno     = 0
			and cb1.delflg    = '0'
			and cb2.cid       = c.cid
			and cb2.delflg    = '0'
			and cb2.bbsid     = cb1.bbsid
		group by
			c.cid,
			cb1.bbsid,
			cb1.comno,
			cb1.mid,
			cb1.title,
			c.title,
			cb1.entdate,
			cb1.entrytype
	) a
/*IF sortcd =='01'*/
order by a.maxupddate desc,a.bbsid
/*END*/
/*IF sortcd =='02'*/
order by a.oyaentddate desc,a.bbsid
/*END*/