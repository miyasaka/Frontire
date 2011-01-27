--ユーザが管理しているグループを取得
select
    a.gid
from
    frontier_group_join as a,
    frontier_group as b
where
    a.frontierdomain = /*frontierdomain*/ and
    a.mid = /*mid*/ and
    a.manageflg = '1' and
    a.frontierdomain = b.frontierdomain and
    a.gid = b.gid and
    b.delflg = '0'
order by gid asc
