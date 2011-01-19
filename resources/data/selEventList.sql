-- topic & event 一覧表示用SQL
select
	cb.cid,
	cb.bbsid,
	cb.title,
	cb.comment,
	cb.pic1,
	cb.pic2,
	cb.pic3,
	coalesce(cb.picnote1,'') as picnote1,
	coalesce(cb.picnote2,'') as picnote2,
	coalesce(cb.picnote3,'') as picnote3,
	cb.mid,
	m.nickname,
	-- 管理人 or トピ設立で編集の可否判断
	case
	when c.admmid = /*mid*/ then '1'
	when cb.mid = /*mid*/ then '1'
	else '0'
	end as editflg,
	count(ijcb.comno)-1 as comcnt,
	to_char(max(ijcb.upddate),'YYYYMMDDHH24MISS') as maxupddate,
	case when to_char(max(ijcb.upddate),'YYYYMMDD') = to_char(current_date,'YYYYMMDD') then to_char(max(ijcb.upddate),'HH24:MI')
	else to_char(max(ijcb.upddate),'MM月DD日')
	end as maxupddatetop,
	cei.event_year,
	cei.event_month,
	cei.event_day,
	coalesce(cei.event_note,'') as event_note,
	cei.eventarea_cd,
	coalesce(cei.eventarea_note,'') as eventarea_note,
	cei.limit_year,
	cei.limit_month,
	cei.limit_day,
	case when ((cei.event_year || cei.event_month || cei.event_day) >= to_char(current_timestamp,'yyyymmdd') and (cei.limit_year || cei.limit_month || cei.limit_day) != '' and (cei.limit_year || cei.limit_month || cei.limit_day) < to_char(current_timestamp,'yyyymmdd')) then '3' --参加者募集締め切り
		 when ((cei.event_year || cei.event_month || cei.event_day) < to_char(current_timestamp,'yyyymmdd')) then '2'   --イベント終了
		 else  '1'
		 end as eventlevel,
	(select count(*) from community_evententry_info where cid = /*cid*/ and bbsid = cb.bbsid) as cntentry
from
	communities as c,
	community_bbs as cb
		inner join
			community_bbs as ijcb
		on
			    ijcb.cid       = cb.cid
			and ijcb.bbsid     = cb.bbsid
			and ijcb.entrytype = cb.entrytype
			and ijcb.delflg    = '0',
	members as m,
	community_event_info cei
where
	    c.cid        = /*cid*/
	and cb.cid       = c.cid
	and cb.cid       = cei.cid
	and cb.bbsid     = cei.bbsid
	and cb.entrytype = /*entrytype*/
	and cb.delflg    = '0'
	and cb.comno     = 0
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
group by
	cb.cid,
	cb.bbsid,
	cb.title,
	cb.comment,
	cb.pic1,
	cb.pic2,
	cb.pic3,
	cb.picnote1,
	cb.picnote2,
	cb.picnote3,
	cb.mid,
	m.nickname,
	editflg,
	cei.event_year,
	cei.event_month,
	cei.event_day,
	cei.event_note,
	cei.eventarea_cd,
	cei.eventarea_note,
	cei.limit_year,
	cei.limit_month,
	cei.limit_day
order by maxupddate desc;