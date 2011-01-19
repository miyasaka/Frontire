select
 c.cid
,c.title
,m.nickname
,ce.sno
,ce.mid
,ce.joinstatus
,ce.requireddate
,ce.requiredmsg
,ce.entdate
,TO_CHAR(ce.upddate,'YYYY年MM月DD日 HH24時MI分') as upddate
,ce.updid
,case mi.mainpicno when '1' then mi.pic1 when '2' then mi.pic2 when '3' then mi.pic3 end as photo
,case when a.logindate < 5 then '5分以内'
when a.logindate < 55 then (trunc(a.logindate/5 + .9, 0)*5)||'分以内'
when a.logindate < 60*23 then ceil(a.logindate/60)||'時間以内'
when a.logindate < 60*24 then '1日以内'
when a.logindate < 60*72 then trunc(a.logindate/60/24)+1||'日以内'
else '3'||'日以上'
end as logindate
from
 communities c,
 community_enterant ce,
    members m left join
    memberphoto_info mi on m.mid = mi.mid left join (select
				mid,
				case to_number(to_char(current_timestamp - lastaccessdate,'ddd'),'99999') when 0 then        to_number(to_char(current_timestamp - lastaccessdate,'hh24'),'9999')*60+to_number(to_char(current_timestamp - lastaccessdate,'mi'),'99') else to_number(to_char(current_timestamp - lastaccessdate,'ddd'),'99999')*60*24 end as logindate
				from
				members) a on m.mid = a.mid
where
    ce.joinstatus = '0'
and ce.mid = m.mid
and c.cid = ce.cid
and c.admmid = /*mid*/
order by ce.upddate