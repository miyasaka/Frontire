--■イベント参加メンバーリスト
select
 ceei.mid
,m.nickname
,case coalesce(mp.mainpicno,'')
	when '1' then mp.pic1
	when '2' then mp.pic2
	when '3' then mp.pic3
else ''
end as pic
from
 community_evententry_info ceei
,community_event_info cei
,members m
,memberphoto_info as mp
where
 cei.cid = /*cid*/
and cei.cid = ceei.cid
and ceei.mid <> /*mid*/
and ceei.mid = m.mid
and m.mid = mp.mid
and cei.bbsid = /*bbsid*/
and cei.bbsid = ceei.bbsid
and m.status = '1'
order by ceei.entdate,ceei.mid