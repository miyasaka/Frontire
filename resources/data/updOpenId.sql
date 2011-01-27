--OpenIDの設定
--初回のOpenID認証の場合にバッチでデータ登録されていた場合
update
	members
set
	openid = /*openid*/,
	updid = /*mid*/,
	upddate = now()
where
	mid = /*mid*/