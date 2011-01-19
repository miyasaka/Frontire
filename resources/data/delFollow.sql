-- フォローしている人を外す(論理削除)
update follow
set
	delflg  = '1',
	updid   = /*updid*/,
	upddate = now()
where
	    followmid   = /*followmid*/
	and followermid = /*followermid*/