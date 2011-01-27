select
	trim(to_char(coalesce(max(to_number(substring(gid,2,9),'000000000')),0)+1,'000000009')) as newid
from
	frontier_group
where
	frontierdomain = /*frontierdomain*/