-- followの削除(更新)
-- ※退会したユーザのフォロー関係情報を更新する
update follow
set
	updid = /*updid*/,
	upddate = now(),
	delflg = '1'
where
	    followmid in ( select mid from members where status = '2')
	and delflg = '0'