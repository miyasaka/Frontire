update
    frontier_group
set
    gname = /*gname*/,
	/*IF pic != null*/
    pic = /*pic*/,
	/*END*/
    joinnumber = (select count(*) from frontier_group_join where frontierdomain = /*frontierdomain*/ and gid = /*gid*/),
    updid = /*updid*/,
    upddate = now()
where
    frontierdomain = /*frontierdomain*/ and 
    gid = /*gid*/