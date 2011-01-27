-- Followテーブルの存在チェック
select count(followmid) as cnt
from follow
where
	    followmid    = /*followmid*/
	and followermid  = /*followermid*/