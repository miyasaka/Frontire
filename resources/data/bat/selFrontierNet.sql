-- 最終バッチ更新処理の日時を返す(初回は2000/01/01 00:00:00を返す)
select
	id,
	network,
	-- nullであれば2000/01/01 00:00:00 を返す(キリが良いから)
	coalesce(to_char(lastdate,'yyyy/mm/dd hh24:mi:ss'),'2000/01/01 00:00:00') as lastdate
from frontiernet