--frontier rss メンバー取得
select
    case
        b.status
    when
        '1' then b.nickname
    else
        b.nickname || '[退]'
    end as nickname
from
    frontier_rss_member as a,
    members as b
where
    a.id = /*id*/ and
    a.mid = b.mid
order by b.status asc,nickname asc