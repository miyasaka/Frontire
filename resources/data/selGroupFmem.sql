--Frontierドメインで絞ったグループ全件。
select
    gid,
    gname,
    pic,
    joinnumber
from
    frontier_group
where
    frontierdomain = /*frontierdomain*/ and
    delflg = '0' and
    joinnumber > 0
order by gname asc,joinnumber desc