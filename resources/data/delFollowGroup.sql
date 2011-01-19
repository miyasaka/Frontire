update
 follow
set
 delflg = '1',
 updid = /*entid*/,
 upddate = now()
where
 followmid = /*mid*/ and
 followermid in (
  select
   mid
  from
   frontier_group_join
  where
   frontierdomain=/*frontierdomain*/ and
   gid=/*gid*/ and
   mid!=/*mid*/
   )
;

update
 follow
set
 delflg = '1',
 updid = /*entid*/,
 upddate = now()
where
 followermid = /*mid*/ and
 followmid in (
  select
   mid
  from
   frontier_group_join
  where
   frontierdomain=/*frontierdomain*/ and
   gid=/*gid*/ and
   mid!=/*mid*/
)
;

