-- Frontierユーザ管理テーブルよりfidを返す
select fid
from frontier_user_management
where
	    frontierdomain = /*fdomain*/
	and mid            = /*mid*/