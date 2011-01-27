--特定のメンバーが設定されているRSSファイルの検索
select
    a.id,
	b.patternname,
	b.filename
from
    frontier_rss_member as a,
    frontier_rss_info as b
where
    a.mid = /*mid*/ and
    a.id = b.id
order by id asc