-- コメントの削除
update community_bbs
set
	delflg = '1',
	upddate = current_timestamp,
	updid = /*mid*/
where
	    cid   = /*cid*/
	and bbsid = /*bbsid*/
	and comno in /*chkcmnt*/(1,2)