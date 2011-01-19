-- followの更新
update follow
set
	confirmflg = /*confirmflg*/,
	updid = /*updid*/,
	upddate = now(),
	delflg = /*delflg*/
where
	    followmid    = /*followmid*/
	and followermid  = /*followermid*/