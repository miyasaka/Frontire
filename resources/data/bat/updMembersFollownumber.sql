-- メンバー情報・フォロー数、フォローワー数の更新
update members
set
	follownumber   = (select count(*) from follow f where f.followmid   = mid and f.delflg = '0'),
	followernumber = (select count(*) from follow f where f.followermid = mid and f.delflg = '0')
-- 自Frontierユーザのみ更新(一応負荷軽減のため)
where membertype = '0'