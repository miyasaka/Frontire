update
    frontier_group
set
    pic = null,
	updid = /*updid*/,
    upddate = now()
where
    frontierdomain = /*frontierdomain*/ and 
    gid = /*gid*/