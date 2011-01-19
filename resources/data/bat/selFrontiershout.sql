-- バッチ更新対象のfrontiershoutデータ取得
select
	fum.frontierdomain,
	fum.fid,
	fs.no,
	fs.comment,
	to_char(fs.entdate,'yyyy/mm/dd hh24:mi:ss') as entdate,
	coalesce(fs.twitter,'0') as twitter,
	coalesce(fs.demandflg,'0') as demandflg,
	coalesce(fs.confirmflg,'0') as confirmflg,
	fs.pub_level,
	coalesce(fs.delflg,'0') as delflg
from
	frontiershout fs,
	members m,
	frontier_user_management fum
where
	    m.mid = fs.mid
	and m.mid = fum.mid
	-- 自Frontierデータか、もしくはリクエスト元のFrontierユーザのデータ
	and (
			   (m.membertype = '0')
			or (m.membertype = '1' and fum.frontierdomain = /*fdomain*/)
		)
	and fs.pub_level in ('0','1')
	and to_char(fs.upddate,'yyyy/mm/dd hh24:mi:ss') >= /*lastdate*/