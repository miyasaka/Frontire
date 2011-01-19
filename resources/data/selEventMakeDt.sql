select
 cb.cid
,cb.bbsid
,cb.comment
,cb.title
,cb.mid
,cei.event_year
,cei.event_month
,cei.event_day
,cei.event_note
,cei.eventarea_note
,cei.limit_year
,cei.limit_month
,cei.limit_day
,
case when ((cei.event_year || cei.event_month || cei.event_day) >= to_char(current_timestamp,'yyyymmdd')) then '1'
	  when ((cei.event_year || cei.event_month || cei.event_day) < to_char(current_timestamp,'yyyymmdd')) then '2'
	  end as eventlevel
from
 community_bbs cb
,community_event_info cei
where
 cb.cid = /*cid*/
and cb.cid = cei.cid
and cb.entrytype = '2'
and cb.mid = /*mid*/
and cb.bbsid = cei.bbsid
and cb.comno = '0'
and (cei.event_year || cei.event_month || cei.event_day) >= to_char(current_timestamp,'yyyymmdd')
and cb.delflg = '0'