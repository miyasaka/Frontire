--イベント参加情報取得
select
 cei.cid
,cei.bbsid
,cei.event_year
,cei.event_month
,cei.event_day
,cei.event_note
,cei.eventarea_note
,cei.limit_year
,cei.limit_month
,cei.limit_day
,ceei.mid
from
 community_event_info cei
,community_evententry_info ceei
,community_bbs cb
where
 cei.cid = /*cid*/
and cei.cid = ceei.cid
and cei.cid = cb.cid
and ceei.mid = /*mid*/
and cei.bbsid = ceei.bbsid
and cei.bbsid = cb.bbsid
and (cei.event_year || cei.event_month || cei.event_day) >= to_char(current_timestamp,'yyyymmdd')
and cb.delflg = '0'
group by
 cei.cid
,cei.bbsid
,cei.event_year
,cei.event_month
,cei.event_day
,cei.event_note
,cei.eventarea_note
,cei.limit_year
,cei.limit_month
,cei.limit_day
,ceei.mid
