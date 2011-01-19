select
mid
from
visitors
where
mid = /*mid*/
and visitmid = /*visitmid*/
and visitday = to_char(current_timestamp,'yyyymmdd');