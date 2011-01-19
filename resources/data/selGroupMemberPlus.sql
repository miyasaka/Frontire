/*IF joincheck != "1"*/
select
    a.manageflg,
    b.mid,
    b.familyname || b.name as name,
    b.email,
    to_char(b.entdate,'yyyy/mm/dd') as entdate,
    b.entdate as sentdate,
    '1' as joinflg
from
    frontier_group_join as a,
    members b
where
    a.frontierdomain = /*frontierdomain*/ and
    a.gid = /*gid*/ and
    a.mid = b.mid and
    b.status = '1' and
	b.membertype = '0'
	/*IF r1 == "1" && searchword != null && searchword != ""*/
	and b.familyname || b.name like '%' || /*searchword*/ || '%' ESCAPE '#'
	/*END*/
	/*IF r1 == "2" && searchword != null && searchword != ""*/
	and b.email like '%' || /*searchword*/ || '%' ESCAPE '#'
	/*END*/

union
/*END*/

select
    '0' as manageflg,
    b.mid,
    b.familyname || b.name as name,
    b.email,
    to_char(b.entdate,'yyyy/mm/dd') as entdate,
    b.entdate as sentdate,
    '0' as joinflg
from
    members b
where
    b.status = '1' and
	b.membertype = '0' and
    not exists (select 
                        a.mid 
                from
                        frontier_group_join as a 
                where 
                        a.mid = b.mid and 
                        a.frontierdomain = /*frontierdomain*/ and 
                        a.gid = /*gid*/
                )
	/*IF r1 == "1" && searchword != null && searchword != ""*/
	and b.familyname || b.name like '%' || /*searchword*/ || '%' ESCAPE '#'
	/*END*/
	/*IF r1 == "2" && searchword != null && searchword != ""*/
	and b.email like '%' || /*searchword*/ || '%' ESCAPE '#'
	/*END*/
/*IF sortname == "1"*/
order by name asc
/*END*/
/*IF sortname == "2"*/
order by name desc
/*END*/
/*IF sortname == "3"*/
order by email asc
/*END*/
/*IF sortname == "4"*/
order by email desc
/*END*/
/*IF sortname == "5"*/
order by sentdate asc
/*END*/
/*IF sortname == "6"*/
order by sentdate desc
/*END*/
/*IF sortname == null*/
order by joinflg desc,manageflg desc,name asc
/*END*/
