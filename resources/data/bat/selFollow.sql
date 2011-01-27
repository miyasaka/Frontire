-- バッチ更新対象のfollowデータ取得
select
	followmid,
	followermid,
	confirmflg,
	to_char(entdate,'yyyy/mm/dd hh24:mi:ss') as entdate,
	coalesce(delflg,'0') as delflg
from follow
where
	    -- フォローしたメンバーが自分のFrontierドメインユーザのデータを取得
	    followmid in (
	    	select m.mid
	    	from
	    		members m,
	    		frontier_user_management fum
	    	where
	    		    m.mid = fum.mid
	    		and m.membertype = '1'
	    		and fum.frontierdomain = /*frontierdomain*/
	    	)
	and to_char(upddate,'yyyy/mm/dd hh24:mi:ss') >= /*lastdate*/