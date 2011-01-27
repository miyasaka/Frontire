select
    gid,
    gname,
    pic,
    joinnumber
from
    frontier_group
where
    frontierdomain = /*frontierdomain*/ and
	/*IF gid != null*/
	gid = /*gid*/ and
	/*END*/
    delflg = '0'
order by gname asc,joinnumber desc