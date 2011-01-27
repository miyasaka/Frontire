select
	f.entdate,
	f.followmid,
	fum.frontierdomain,
	fum.fid,
	fum.nickname as fnickname,
	m.nickname
from
	follow f,
	members m,
	frontier_user_management fum
where
	    f.followermid  = /*mid*/
	and f.confirmflg   = '0'
	and f.followmid    = m.mid
	and m.status       = '1'
	and fum.mid        = m.mid
order by f.entdate