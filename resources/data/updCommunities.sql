-- コミュニティの設定更新
update communities
set
	title        = /*title*/,
	detail       = /*detail*/,
	category1    = /*category1*/,
	joincond     = /*joincond*/,
	publevel     = /*publevel*/,
	makabletopic = /*makabletopic*/,
	pic          = /*pic*/,
	upddate      = /*upddate*/,
	updid        = /*updid*/
where cid = /*cid*/