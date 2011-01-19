-- フォローしている人を確認時の更新ＳＱＬ
update follow
set confirmflg = '1'
where
	    followmid   = /*followmid*/
	and followermid = /*followermid*/
	and confirmflg != '1'