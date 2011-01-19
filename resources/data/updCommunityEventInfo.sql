-- コミュニティイベント情報の更新
update community_event_info
set
	event_year     = /*eventYear*/,
	event_month    = /*eventMonth*/,
	event_day      = /*eventDay*/,
	event_note     = /*eventNote*/,
	eventarea_note = /*eventareaNote*/,
	limit_year     = /*limitYear*/,
	limit_month    = /*limitMonth*/,
	limit_day      = /*limitDay*/
where
	    cid       = /*cid*/
	and bbsid     = /*bbsid*/
