-- Frontierユーザ管理テーブルの存在チェック(あればmidを返す)
select mid
from frontier_user_management
where
	    frontierdomain = /*fdomain*/
	and fid            = /*fid*/