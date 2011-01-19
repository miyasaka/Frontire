-- event 選択されたメンバー表示用SQL
select
 m.mid
,m.nickname
from
 community_event_info cei
,community_evententry_info ceei
,members m
where
    cei.cid = ceei.cid
and cei.cid = /*cid*/
and cei.bbsid = ceei.bbsid
and cei.bbsid = /*bbsid*/
and ceei.mid in /*chkmem*/(1,2)
and ceei.mid = m.mid
order by ceei.entdate,ceei.mid