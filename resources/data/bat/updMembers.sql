-- メンバー情報の更新
update members
set
	status  = /*status*/,
	updid   = /*updid*/,
	upddate = now()
where mid = /*mid*/