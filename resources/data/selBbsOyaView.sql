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
	to_char(cb.entdate,'yyyymmddhh24mi') as entdate,
	coalesce(cb.picnote1,'') as picnote1,
	coalesce(cb.picnote2,'') as picnote2,
	coalesce(cb.picnote3,'') as picnote3
from
	communities as c,
	community_bbs as cb,
	members as m
where
	    c.cid        = cb.cid
	and c.cid        = /*cid*/
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