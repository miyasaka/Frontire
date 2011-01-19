-- topic & event 閲覧情報取得用SQL
select
	cb.bbsid,
	cb.comno,
	cb.title,
	cb.comment,
	cb.pic1,
	cb.pic2,
	cb.pic3,
	cb.mid,
	m.nickname,
	-- 管理人 or トピ設立で編集の可否判断
	case
	when c.admmid = /*mid*/ then '1'
	when cb.mid = /*mid*/ then '1'
	else '0'
	end as editflg,
	-- イベント作成判断
	case
	when cb.mid = /*mid*/ then '1'
	else '0'
	end as eventmaker,
	to_char(cb.entdate,'yyyymmddhh24mi') as entdate,
	coalesce(cb.picnote1,'') as picnote1,
	coalesce(cb.picnote2,'') as picnote2,
	coalesce(cb.picnote3,'') as picnote3,
	coalesce(cei.event_year,'') as event_year,
	coalesce(cei.event_month,'') as event_month,
	coalesce(cei.event_day,'') as event_day,
	coalesce(cei.event_note,'') as event_note,
	coalesce(cei.eventarea_note,'') as eventarea_note,
	coalesce(cei.limit_year,'') as limit_year,
	coalesce(cei.limit_month,'') as limit_month,
	coalesce(cei.limit_day,'') as limit_day,
	(select count(*) from community_evententry_info where cid = /*cid*/ and bbsid = /*bbsid*/) as cntentry,
	case when ((cei.event_year || cei.event_month || cei.event_day) >= to_char(current_timestamp,'yyyymmdd')) then '1'
		 when ((cei.event_year || cei.event_month || cei.event_day) < to_char(current_timestamp,'yyyymmdd')) then '2'
		 end as eventlevel,
    case when ((cei.event_year || cei.event_month || cei.event_day) >= to_char(current_timestamp,'yyyymmdd') and (cei.limit_year || cei.limit_month || cei.limit_day) != '' and (cei.limit_year || cei.limit_month || cei.limit_day) < to_char(current_timestamp,'yyyymmdd')) then '1' --参加者募集締め切り
    	 else '0'
         end as eventlimitover
from
	communities as c,
	community_bbs as cb,
	members as m,
    community_event_info cei
where
	    c.cid        = cb.cid
	and c.cid        = cei.cid
	and c.cid        = /*cid*/
	and cb.bbsid     = cei.bbsid
	and cb.bbsid     = /*bbsid*/
	and cb.entrytype = /*entrytype*/
	and cb.comno     = 0
	and cb.delflg    ='0'
	and cb.mid       = m.mid
	-- 公開レベル条件
	and (
		    -- 公開
		    c.publevel = '0'
		    -- 非公開
		or (
		    c.publevel != '0'
		    and  exists (
				-- コミュニティに参加してるかどうか
				select
					ce.cid
				from
					community_enterant as ce
				where
					    ce.cid = c.cid
					and ce.mid = /*mid*/
					and joinstatus = '1'
			)
		)
	)