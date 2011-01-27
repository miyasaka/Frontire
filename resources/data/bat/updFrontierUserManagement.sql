-- Frontierユーザ管理の更新
update frontier_user_management
set
	nickname = /*nickname*/,
	pic      = /*pic*/,
	updid    = /*updid*/,
	upddate  = now()
where mid = /*mid*/