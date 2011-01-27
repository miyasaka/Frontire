-- コミュニティ詳細情報取得
select
	c.cid,
	c.title,
	coalesce(c.pic,'') as pic,
	c.admmid,
	m.nickname,
	m.status,
	c.detail,
	c.category1,
	mc.itemname,
	c.joincond,
	c.publevel,
	c.makabletopic,
	to_char(c.entdate,'YYYYMMDD') as entdate,
	case c.joincond
		when '0' then 'なし'
		else '管理人の許可が必要'
	end as jointxt,
	case c.publevel
		when '0' then '全てに公開'
		else '非公開'
	end as pubtxt,
	case c.makabletopic
		when '0' then '参加者が作成可能'
		else '管理人のみ作成可能'
	end as authtxt,
	extract(day from current_timestamp - c.entdate) as periodday,
	extract(hour from current_timestamp - c.entdate) as periodhour
from
	communities as c,
	members as m,
	mst_comitemcode as mc
where
	    c.cid       = /*cid*/
	and c.admmid    = m.mid
	and mc.itemid   = 'category'
	and c.category1 = mc.itemcd