--RSSファイルのメンバーとその他のメンバー一覧
select
    b.mid,
    b.nickname,
    b.familyname || b.name as name,
    case
        b.status
    when
        '2' then '[退]'
    end as statusname,
    to_char(b.entdate,'yyyy/mm/dd') as entdate,
    b.entdate as sortentdate,
    b.status as status,
    '1' as joinflg
from
    frontier_rss_member as a,
    members as b
	/*IF searchgroup != null && searchgroup != ""*/
    	,frontier_group_join as c
    	,frontier_group as d
	/*END*/
where
    a.id = /*id*/ and
    a.mid = b.mid
	/*IF r1 == "1" && searchword != null && searchword != ""*/
		and b.nickname like '%' || /*searchword*/ || '%' ESCAPE '#'
	/*END*/
	/*IF r1 == "2" && searchword != null && searchword != ""*/
		and b.familyname || b.name like '%' || /*searchword*/ || '%' ESCAPE '#'
	/*END*/
	/*IF searchgroup != null && searchgroup != ""*/
    	and b.mid = c.mid
    	and c.frontierdomain = d.frontierdomain
    	and c.gid = d.gid
    	and d.gid = /*searchgroup*/
	/*END*/
/*IF searchFlg == "1" */
union
select
    b.mid,
    b.nickname,
    b.familyname || b.name as name,
    case
        b.status
    when
        '2' then '[退]'
    end as statusname,
    to_char(b.entdate,'yyyy/mm/dd') as entdate,
    b.entdate as sortentdate,
    b.status as status,
    '0' as joinflg
from
    members as b
	/*IF searchgroup != null && searchgroup != ""*/
    	,frontier_group_join as c
    	,frontier_group as d
	/*END*/
where
    b.membertype = '0' and
    b.status = '1' and
    not exists (select 
    	           e.mid 
                from
                   frontier_rss_member as e 
                where 
                   e.mid = b.mid and
                   e.id = /*id*/
         		)
	/*IF r1 == "1" && searchword != null && searchword != ""*/
		and b.nickname like '%' || /*searchword*/ || '%' ESCAPE '#'
	/*END*/
	/*IF r1 == "2" && searchword != null && searchword != ""*/
		and b.familyname || b.name like '%' || /*searchword*/ || '%' ESCAPE '#'
	/*END*/
	/*IF searchgroup != null && searchgroup != ""*/
    	and b.mid = c.mid
    	and c.frontierdomain = d.frontierdomain
    	and c.gid = d.gid
    	and d.gid = /*searchgroup*/
	/*END*/
/*END*/
/*IF sortname == "1"*/
order by nickname asc,joinflg desc,status asc
/*END*/
/*IF sortname == "2"*/
order by nickname desc,joinflg desc,status asc
/*END*/
/*IF sortname == "3"*/
order by sortentdate asc,joinflg desc,status asc
/*END*/
/*IF sortname == "4"*/
order by sortentdate desc,joinflg desc,status asc
/*END*/
/*IF sortname == null*/
order by joinflg desc,status asc,nickname asc
/*END*/
