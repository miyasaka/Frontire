-- 最終取得日時の更新(バッチが最後に走った日時)
update frontiernet
set lastdate = now()
where
	    id      = /*id*/
	and network = /*network*/