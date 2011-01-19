-- Frontier Shoutの更新
update frontiershout
set
	updid      = /*updid*/,
	upddate    = now(),
	confirmflg = /*confirmflg*/,
	delflg     = /*delflg*/
where
	    mid = /*mid*/
	and no  = /*no*/