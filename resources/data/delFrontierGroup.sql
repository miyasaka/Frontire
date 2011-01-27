update
    frontier_group
set
    delflg = '1',
    updid = /*updid*/,
    upddate = now()
where
    frontierdomain = /*frontierdomain*/ and
    gid = /*gid*/