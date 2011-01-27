-- コミュニティ情報取得
select
	c.cid,
	c.title,
	coalesce(c.pic,'') as pic,
	-- 管理人か、もしくは誰でも掲示板作成できるか
	-- 0 : 非会員
	-- 1 : 管理人
	-- 2 : 会員(作成権限あり)
	-- 3 : 会員(作成権限なし)
	case
		when ce.mid is null then '0'
		else
			case
			when c.admmid = ce.mid then '1'
			when c.makabletopic = '0' then '2'
			else '3'
			end
	end as makabletopic,
	joincond
from
	communities as c left join
		community_enterant as ce on
		    ce.cid        = c.cid
		and ce.mid        = /*mid*/
		and ce.joinstatus = '1'
where
	c.cid = /*cid*/