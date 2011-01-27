--RSS設定情報を全件取得するSQL
select
    a.id,
    a.patternname,
    a.detail
from
    frontier_rss_info as a
order by a.patternname